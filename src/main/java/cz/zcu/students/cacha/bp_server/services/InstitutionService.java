package cz.zcu.students.cacha.bp_server.services;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.Institution;
import cz.zcu.students.cacha.bp_server.domain.Language;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.exceptions.CannotPerformActionException;
import cz.zcu.students.cacha.bp_server.exceptions.CannotSaveImageException;
import cz.zcu.students.cacha.bp_server.exceptions.NotFoundException;
import cz.zcu.students.cacha.bp_server.repositories.InstitutionRepository;
import cz.zcu.students.cacha.bp_server.repositories.LanguageRepository;
import cz.zcu.students.cacha.bp_server.repositories.RoleRepository;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import cz.zcu.students.cacha.bp_server.view_models.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.bp_server.assets_store_config.WebConfiguration.DEFAULT_EXHIBIT_IMAGE;
import static cz.zcu.students.cacha.bp_server.assets_store_config.WebConfiguration.DEFAULT_INSTITUTION_IMAGE;
import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.ROLE_INSTITUTION_OWNER;
import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.ROLE_TRANSLATOR;

/**
 * Class represent service which is responsible for institution operations
 */
@Service
public class InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Gets all institutions
     * @return all institutions
     */
    public List<InstitutionVM> getInstitutions() {
        // get all institutions ordered by name
        List<InstitutionVM> institutions = institutionRepository.findAll().stream().map(InstitutionVM::new).sorted(Comparator.comparing(InstitutionVM::getName))
                .collect(Collectors.toList());
        return institutions;
    }

    /**
     * Saves institution with given user as a manager
     * @param institution new institution
     * @param user institution manager
     */
    public void saveInstitution(Institution institution, User user) {
        // check if the user already have an institution
        if(user.isInstitutionOwner()) {
            throw new CannotPerformActionException("User already owns an institution");
        }

        // convert string coordinates to double
        institution.setLatitude(Double.parseDouble(institution.getLatitudeString()));
        institution.setLongitude(Double.parseDouble(institution.getLongitudeString()));

        // if institution image is not null, save it in the file system
        if(institution.getEncodedImage() != null) {
            String imageName;

            try {
                imageName = fileService.saveInstitutionImage(institution.getEncodedImage());
                institution.setImage(imageName);
            } catch (Exception exception) {
                throw new CannotSaveImageException("Image could not be saved");
            }
        }

        // save institution
        institutionRepository.save(institution);

        // set user as institution manager and give him appropriate role
        user.setInstitution(institution);
        user.getRoles().add(roleRepository.findByName(ROLE_INSTITUTION_OWNER).get());
        userRepository.save(user);
    }

    /**
     * Gets the chosen and possible languages of institution
     * @param user institution owner
     * @return chosen and possible languages
     */
    public AllowedLanguagesVM getAllowedLanguages(User user) {
        Set<Language> possibleLanguages = languageRepository.findAllByOrderByName();
        Set<Language> chosenLanguages = user.getInstitution().getLanguages();
        // remove chosen languages from all languages in db
        possibleLanguages.removeAll(chosenLanguages);
        return new AllowedLanguagesVM(possibleLanguages, chosenLanguages);
    }

    /**
     * Adds language to institution
     * @param languageId language id to add
     * @param user institution owner
     */
    public void addLanguage(Long languageId, User user) {
        // find language by id
        Optional<Language> languageOptional = languageRepository.findById(languageId);
        if(languageOptional.isEmpty()) {
            throw new NotFoundException("Language not found");
        }

        // return if institution already contains this language
        Institution institution = user.getInstitution();
        if(institution.getLanguages().contains(languageOptional.get())) {
            return;
        }

        // add language to institution and save it
        institution.getLanguages().add(languageOptional.get());
        institutionRepository.save(institution);
    }

    /**
     * Updates institution image
     * @param imageVM encoded image
     * @param user logged in user
     * @return new image name
     */
    public String updateImage(ImageVM imageVM, User user) {
        Institution institution = user.getInstitution();
        if(!institution.getImage().equals(DEFAULT_INSTITUTION_IMAGE)) {
            // delete old image
            fileService.deleteInstitutionImage(institution.getImage());
        }

        String imageName;
        try {
            // save new image
            imageName = fileService.saveInstitutionImage(imageVM.getEncodedImage());
        } catch (Exception exception) {
            throw new CannotSaveImageException("Image could not be saved");
        }

        // set new image name and save institution
        institution.setImage(imageName);
        institutionRepository.save(institution);
        return imageName;
    }

    /**
     * Updates institution information
     * @param institution updated institution
     * @param user logged in user
     */
    public void updateInstitution(UpdateInstitutionVM institution, User user) {
        // set updated information
        Institution userInstitution = user.getInstitution();
        userInstitution.setName(institution.getName());
        userInstitution.setAddress(institution.getAddress());
        userInstitution.setLatitude(Double.parseDouble(institution.getLatitudeString()));
        userInstitution.setLongitude(Double.parseDouble(institution.getLongitudeString()));
        // save institution
        institutionRepository.save(userInstitution);
    }

    /**
     * Gets user's institution
     * @param user owner
     * @return user's institution
     */
    public InstitutionVM getMyInstitution(User user) {
        // check if user manages an institution
        if(!user.isInstitutionOwner()) {
            throw new CannotPerformActionException("User does not own an institution");
        }

        return new InstitutionVM(user.getInstitution());
    }

    /**
     * Adds new institution manager by sending credentials of a new manager account to given email
     * @param emailVM email of a new institution manager
     * @param user institution manager
     */
    public void addInstitutionManager(EmailVM emailVM, User user) {
        Institution institution = user.getInstitution();

        // generate unique username
        String username = Long.toString(new Date().getTime());
        while(userRepository.findByUsername(username).isPresent()) {
            username = Long.toString(new Date().getTime());
        }

        // generate strong password
        String password = RandomStringUtils.randomAlphabetic(64);

        // create a new user and set his properties accordingly
        User newManager = new User();
        newManager.setUsername(username);
        newManager.setPassword(bCryptPasswordEncoder.encode(password));
        newManager.setEmail(emailVM.getEmail());

        // let new user manage given institution
        newManager.setInstitution(institution);

        // add translator and institution owner roles
        newManager.getRoles().add(roleRepository.findByName(ROLE_TRANSLATOR).get());
        newManager.getRoles().add(roleRepository.findByName(ROLE_INSTITUTION_OWNER).get());

        try {
            // send mail with new account information
            emailService.sendSimpleMessage(emailVM.getEmail(), "Institution manager credentials",
                    "You have been granted managerial rights to a cultural institution registered in the system for community translations of information texts - "
                            + institution.getName() + ". The credentials are as follows:\n\nusername: " + username + "\n" + "password: " + password +
                            "\n\nYou can change the credentials in profile settings after logging in to the system.\n\nThis mail is automatically generated. Do not respond to it." +
                            "\n\nRegards,\nCommunity Translation System");
        }
        catch (Exception e) {
            throw new CannotPerformActionException("Failed to send email with new account");
        }

        // after sending mail save new manager to db
        userRepository.save(newManager);
    }

    /**
     * Deletes user's institution
     * @param user institution manager
     */
    public void deleteMyInstitution(User user) {
        Institution institution = user.getInstitution();
        deleteInstitution(institution);
    }

    /**
     * Deletes given institution
     * @param institution institution to delete
     */
    public void deleteInstitution(Institution institution) {
        // delete institution image if exits
        if(!institution.getImage().equals(DEFAULT_INSTITUTION_IMAGE)) {
            fileService.deleteInstitutionImage(institution.getImage());
        }

        // delete images and info labels of all exhibits
        for(Exhibit exhibit : institution.getExhibits()) {
            // if exhibit image is not default delete it
            if(!exhibit.getImage().equals(DEFAULT_EXHIBIT_IMAGE)) {
                fileService.deleteExhibitImage(exhibit.getImage());
            }
            // delete info label from fs
            fileService.deleteInfoLabelImage(exhibit.getInfoLabel());
        }

        // remove institution managers
        for(User u : institution.getOwners()) {
            // remove institution owner role
            u.getRoles().remove(roleRepository.findByName(ROLE_INSTITUTION_OWNER).get());
            // remove institution link
            u.setInstitution(null);
            userRepository.save(u);
        }

        // delete institution
        institutionRepository.delete(institution);
    }

    /**
     * Gets all institutions ordered relative to given coordinates
     * @param coordinates coordinates
     * @return ordered institutions
     */
    public List<InstitutionVM> getInstitutionsOrdered(CoordinatesVM coordinates) {
        // convert coordinates to double values
        double latitude = Double.parseDouble(coordinates.getLatitude());
        double longitude = Double.parseDouble(coordinates.getLongitude());

        // find all institutions
        List<Institution> institutionsList = institutionRepository.findAll();

        // calculate distance of all institutions to given coordinates
        institutionsList.forEach(i -> i.setDistance(distance(i.getLatitude(), latitude, i.getLongitude(), longitude)));
        // sort institutions by calculated distance
        institutionsList.sort(Comparator.comparingDouble(Institution::getDistance));

        return institutionsList.stream().map(InstitutionVM::new).collect(Collectors.toList());
    }

    /**
     * Calculate distance between two pints on earth
     * @param lat1 latitude of first point
     * @param lat2 latitude of second point
     * @param lon1 longitude of first point
     * @param lon2 longitude of second point
     * @return distance between two points on earth
     */
    private double distance(double lat1, double lat2, double lon1, double lon2) {
        // earth radius
        final int R = 6371;

        // get latitude and longitude distinctions
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        // calculate distance between points
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // convert to meters
        double distance = R * c * 1000;
        return distance;
    }
}