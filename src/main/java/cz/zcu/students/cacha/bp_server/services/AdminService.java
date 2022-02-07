package cz.zcu.students.cacha.bp_server.services;

import cz.zcu.students.cacha.bp_server.domain.Institution;
import cz.zcu.students.cacha.bp_server.domain.Role;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.exceptions.CannotPerformActionException;
import cz.zcu.students.cacha.bp_server.exceptions.NotFoundException;
import cz.zcu.students.cacha.bp_server.exceptions.ValidationErrorException;
import cz.zcu.students.cacha.bp_server.repositories.RoleRepository;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import cz.zcu.students.cacha.bp_server.view_models.BooleanValVM;
import cz.zcu.students.cacha.bp_server.view_models.UserDetailVM;
import cz.zcu.students.cacha.bp_server.view_models.UserVM;
import cz.zcu.students.cacha.bp_server.view_models.UsernameUpdateVM;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.ROLE_INSTITUTION_OWNER;
import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.ROLE_TRANSLATOR;

/**
 * Class represent service which is responsible for administrator operations
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Gets all users except admins
     * @return all users except admins
     */
    public List<UserVM> getUsers() {
        // get non admin users and return it
        List<User> nonAdminUsers = userRepository.getNonAdminUsers();
        return nonAdminUsers.stream().map(UserVM::new).collect(Collectors.toList());
    }

    /**
     * Gets the details about given user
     * @param userId user id
     * @return details about given user
     */
    public UserDetailVM getUserDetail(Long userId) {
        // find user and check if user is not admin
        User user = verifyUser(userId);
        return new UserDetailVM(user);
    }

    /**
     * Updates username to given user
     * @param usernameUpdateVM new username
     * @param userId user id
     */
    public void updateUserUsername(UsernameUpdateVM usernameUpdateVM, Long userId) {
        // check if user exists
        User user = verifyUser(userId);

        // check if username is free
        String newUsername = usernameUpdateVM.getUsername();
        Optional<User> userWithUsernameOptional = userRepository.findByUsername(newUsername);
        if(userWithUsernameOptional.isPresent() && !userWithUsernameOptional.get().getId().equals(user.getId())) {
            // if username take throw error
            HashMap<String, String> errorsMap = new HashMap<>();
            errorsMap.put("username", "Username is already taken");
            throw new ValidationErrorException(errorsMap);
        }

        // set value and save user
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    /**
     * Checks if user exists and it is not admin
     * @param userId user id
     * @return found user
     */
    private User verifyUser(Long userId) {
        // try to find user in db
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            // if user not exists throw exception
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        // check if user is admin and if so throw exception
        if(user.isAdmin()) {
            throw new CannotPerformActionException("Can not return administrator");
        }

        return user;
    }

    /**
     * Generates new password for given user and sends it to his mail
     * @param userId user id
     */
    public void updateUserPassword(Long userId) {
        // check if user exists
        User user = verifyUser(userId);
        // generate new password
        String password = RandomStringUtils.randomAlphabetic(64);

        try {
            // send mail with new password
            emailService.sendSimpleMessage(user.getEmail(), "New password",
                    "The password to your account registered in the system for community translations of information texts was changed by the administrator."
                            + " The credentials are now as follows:\n\nusername: " + user.getUsername() + "\n" + "password: " + password +
                            "\n\nYou can change the credentials in profile settings after logging in to the system.\n\nThis mail is automatically generated. Do not respond to it." +
                            "\n\nRegards,\nCommunity Translation System");
        }
        catch (Exception e) {
            throw new CannotPerformActionException("Failed to send email with new password");
        }

        // set new password and save user
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    /**
     * Changes the value of user translator rights
     * @param booleanValVM new user rights value
     * @param userId user id
     */
    public void setTranslator(BooleanValVM booleanValVM, Long userId) {
        // check if user exists
        User user = verifyUser(userId);
        boolean value = booleanValVM.getValue();

        // if new value is the same as the in db, the state is ok
        boolean isTranslator = user.isTranslator();
        if((isTranslator && value) || (!isTranslator && !value)) {
            return;
        }

        if(value) {
            // add translator role
            Role role = roleRepository.findByName(ROLE_TRANSLATOR).get();
            user.getRoles().add(role);
        } else {
            // remove translator role
            user.setRoles(user.getRoles().stream().filter(role -> !role.getName().equals(ROLE_TRANSLATOR)).collect(Collectors.toSet()));
        }

        // save user
        userRepository.save(user);
    }

    /**
     * Removes user's managerial rights to his institution
     * @param userId user id
     */
    public void removeInstitution(Long userId) {
        // checks if user exists
        User user = verifyUser(userId);

        // if user does not manage an institution it is ok
        if(user.getInstitution() == null) {
            return;
        }

        Institution institution = user.getInstitution();
        if(institution.getOwners().size() > 1) {
            // if there are more managers no need to delete institution
            user.setInstitution(null);
            // remove owner role and save user
            user.setRoles(user.getRoles().stream().filter(role -> !role.getName().equals(ROLE_INSTITUTION_OWNER)).collect(Collectors.toSet()));
            userRepository.save(user);
        }
        else {
            // if there is just one manager - delete whole institution
            institutionService.deleteInstitution(institution);
        }
    }

    /**
     * Changes if user is banned or not depending on given value
     * @param booleanValVM new ban value
     * @param userId user id
     */
    public void setBan(BooleanValVM booleanValVM, Long userId) {
        // check if user exists
        User user = verifyUser(userId);
        // set new ban value
        user.setBanned(booleanValVM.getValue());
        // save user
        userRepository.save(user);
    }
}
