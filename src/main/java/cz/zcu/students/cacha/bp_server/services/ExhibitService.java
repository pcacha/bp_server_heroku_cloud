package cz.zcu.students.cacha.bp_server.services;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.Institution;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.exceptions.CannotPerformActionException;
import cz.zcu.students.cacha.bp_server.exceptions.CannotSaveImageException;
import cz.zcu.students.cacha.bp_server.exceptions.NotFoundException;
import cz.zcu.students.cacha.bp_server.repositories.ExhibitRepository;
import cz.zcu.students.cacha.bp_server.repositories.InstitutionRepository;
import cz.zcu.students.cacha.bp_server.view_models.ExhibitVM;
import cz.zcu.students.cacha.bp_server.view_models.ExhibitsLanguagesVM;
import cz.zcu.students.cacha.bp_server.view_models.ImageVM;
import cz.zcu.students.cacha.bp_server.view_models.UpdateExhibitVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.bp_server.assets_store_config.WebConfiguration.DEFAULT_EXHIBIT_IMAGE;

/**
 * Class represent service which is responsible for exhibits operations
 */
@Service
public class ExhibitService {

    @Autowired
    private ExhibitRepository exhibitsRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private QRCodeService qrCodeService;

    /**
     * Gets all exhibits of given institution
     * @param institutionId institution id
     * @return all exhibits of given institution
     */
    public List<ExhibitVM> getExhibitsOfInstitution(Long institutionId) {
        // check if institution with this id exists
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if(institutionOptional.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        // get all exhibits and return them sorted by name
        List<ExhibitVM> exhibits = institutionOptional.get().getExhibits().stream().map(ExhibitVM::new).sorted(Comparator.comparing(ExhibitVM::getName))
                .collect(Collectors.toList());
        return exhibits;
    }

    /**
     * Deletes exhibit if is managed by given user
     * @param exhibitId exhibit id
     * @param user user
     */
    public void deleteExhibit(Long exhibitId, User user) {
        // check if exhibit is managed by given user
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);
        // delete exhibit
        deleteExhibit(exhibit);
    }

    /**
     * Deletes given exhibit from db and its images from fs
     * @param exhibit exhibit
     */
    public void deleteExhibit(Exhibit exhibit) {
        // if exhibit image is not default delete it
        if(!exhibit.getImage().equals(DEFAULT_EXHIBIT_IMAGE)) {
            fileService.deleteExhibitImage(exhibit.getImage());
        }
        // delete info label from fs
        fileService.deleteInfoLabelImage(exhibit.getInfoLabel());

        // delete exhibit from db
        exhibitsRepository.delete(exhibit);
    }

    /**
     * Checks if given user manages given exhibit and returns the exhibit
     * @param exhibitId exhibit id
     * @param user user
     * @return exhibit selected by its id
     */
    private Exhibit verifyUserManagesExhibit(Long exhibitId, User user) {
        // get exhibit if exists
        Optional<Exhibit> exhibitOptional = exhibitsRepository.findById(exhibitId);
        if(exhibitOptional.isEmpty()) {
            throw new NotFoundException("Exhibit not found");
        }
        Exhibit exhibit = exhibitOptional.get();

        // check if user is manager of given exhibit
        if(!user.isInstitutionOwner() || !exhibit.getInstitution().getId().equals(user.getInstitution().getId())) {
            throw new CannotPerformActionException("Logged in user does not have permission to delete this exhibit");
        }
        return exhibit;
    }

    /**
     * Saves new exhibit to user's institution
     * @param exhibit new exhibit
     * @param user owner of an institution
     */
    public void saveExhibit(Exhibit exhibit, User user) {
        saveExhibitToInstitution(exhibit, user.getInstitution());
    }

    /**
     * Saves new exhibit to institution defined by its id
     * @param exhibit new exhibit
     * @param institutionId id of institution managing exhibit
     */
    public void saveExhibit(Exhibit exhibit, Long institutionId) {
        // check if institution exists
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if(institutionOptional.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        // save exhibit
        saveExhibitToInstitution(exhibit, institutionOptional.get());
    }

    /**
     * Saves new exhibit to given institution
     * @param exhibit new exhibit
     * @param institution institution owning the exhibit
     */
    private void saveExhibitToInstitution(Exhibit exhibit, Institution institution) {
        if(exhibit.getEncodedImage() != null) {
            // if exhibit image is also included
            String imageName;

            try {
                // save image and set it to exhibit
                imageName = fileService.saveExhibitImage(exhibit.getEncodedImage());
                exhibit.setImage(imageName);
            } catch (Exception exception) {
                throw new CannotSaveImageException("Image could not be saved");
            }
        }

        String infoLabelName;
        try {
            // save info label image and set it to exhibit
            infoLabelName = fileService.saveInfoLabelImage(exhibit.getEncodedInfoLabel());
            exhibit.setInfoLabel(infoLabelName);
        } catch (Exception exception) {
            throw new CannotSaveImageException("Info label could not be saved");
        }

        // set institution and save exhibit
        exhibit.setInstitution(institution);
        exhibitsRepository.save(exhibit);
    }

    /**
     * Updates exhibit image and returns its new name
     * @param exhibitId updated exhibit id
     * @param imageVM encoded image
     * @param user manager of given exhibit
     * @return new image name
     */
    public String updateExhibitImage(Long exhibitId, ImageVM imageVM, User user) {
        // verify that user is manager of given exhibit
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);

        // delete old image if exists
        if(!exhibit.getImage().equals(DEFAULT_EXHIBIT_IMAGE)) {
            fileService.deleteExhibitImage(exhibit.getImage());
        }

        // save new image
        String imageName;
        try {
            imageName = fileService.saveExhibitImage(imageVM.getEncodedImage());
        } catch (Exception exception) {
            throw new CannotSaveImageException("Image could not be saved");
        }

        // set new name and save exhibit
        exhibit.setImage(imageName);
        exhibitsRepository.save(exhibit);
        return imageName;
    }

    /**
     * Updates exhibit info label image and returns its new name
     * @param exhibitId updated exhibit id
     * @param imageVM encoded info label image
     * @param user manager of given exhibit
     * @return new info label name
     */
    public String updateExhibitInfoLabel(Long exhibitId, ImageVM imageVM, User user) {
        // verify that user is manager of given exhibit
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);
        // delete old info label
        fileService.deleteInfoLabelImage(exhibit.getInfoLabel());

        // save new info label
        String infoLabelName;
        try {
            infoLabelName = fileService.saveInfoLabelImage(imageVM.getEncodedImage());
        } catch (Exception exception) {
            throw new CannotSaveImageException("Info label could not be saved");
        }

        // set new name and save exhibit
        exhibit.setInfoLabel(infoLabelName);
        exhibitsRepository.save(exhibit);
        return infoLabelName;
    }

    /**
     * Updates exhibit information
     * @param exhibitId updated exhibit id
     * @param updateExhibitVM updated information
     * @param user manager of given exhibit
     */
    public void updateExhibit(Long exhibitId, UpdateExhibitVM updateExhibitVM, User user) {
        // verify that user is manager of given exhibit
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);
        // set updated information
        exhibit.setName(updateExhibitVM.getName());
        exhibit.setInfoLabelText(updateExhibitVM.getInfoLabelText());
        exhibit.setBuilding(updateExhibitVM.getBuilding());
        exhibit.setRoom(updateExhibitVM.getRoom());
        exhibit.setShowcase(updateExhibitVM.getShowcase());
        // save exhibit
        exhibitsRepository.save(exhibit);
    }

    /**
     * Gets all the exhibits of user's institution
     * @param user owner of an institution
     * @return all exhibits of user's institution
     */
    public List<ExhibitVM> getAllExhibitsOfUsersInstitution(User user) {
        // return all exhibits mapped to view model and sorted by name
        return user.getInstitution().getExhibits().stream().map(ExhibitVM::new).sorted(Comparator.comparing(ExhibitVM::getName)).collect(Collectors.toList());
    }

    /**
     * Gets exhibits and allowed languages of user's institution
     * @param user owner of institution
     * @return exhibits and allowed language
     */
    public ExhibitsLanguagesVM getExhibitsApproveTranslations(User user) {
        return new ExhibitsLanguagesVM(user.getInstitution().getLanguages(), user.getInstitution().getExhibits());
    }

    /**
     * Gets an exhibit defined by its id if is managed by given user
     * @param exhibitId exhibit id
     * @param user manager of exhibit
     * @return exhibit
     */
    public ExhibitVM getExhibit(Long exhibitId, User user) {
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);
        return new ExhibitVM(exhibit);
    }

    /**
     * Gets base64 encoded QR code for given exhibit
     * @param exhibitId exhibit id
     * @param user institution manager
     * @return base64 encoded QR code
     */
    public String getExhibitQRCode(Long exhibitId, User user) {
        // check exhibit exists
        Exhibit exhibit = verifyUserManagesExhibit(exhibitId, user);

        try {
            // get image of qr code
            BufferedImage image = qrCodeService.generateQRCodeImage(exhibit.getId().toString());
            // get byte array from image
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            // encode image to base64 and return it
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (Exception e) {
            throw new CannotPerformActionException("QR code generation failed");
        }
    }

    /**
     * Gets exhibits and allowed languages of an institution defined by its id
     * @param institutionId institution id
     * @return exhibits and allowed language
     */
    public ExhibitsLanguagesVM getExhibitsTranslate(Long institutionId) {
        // check if institution exists
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if(institutionOptional.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        return new ExhibitsLanguagesVM(institutionOptional.get().getLanguages(), institutionOptional.get().getExhibits());
    }
}
