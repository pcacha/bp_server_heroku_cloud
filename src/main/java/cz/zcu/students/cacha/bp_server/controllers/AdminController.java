package cz.zcu.students.cacha.bp_server.controllers;

import cz.zcu.students.cacha.bp_server.responses.GenericResponse;
import cz.zcu.students.cacha.bp_server.services.AdminService;
import cz.zcu.students.cacha.bp_server.view_models.BooleanValVM;
import cz.zcu.students.cacha.bp_server.view_models.UserDetailVM;
import cz.zcu.students.cacha.bp_server.view_models.UserVM;
import cz.zcu.students.cacha.bp_server.view_models.UsernameUpdateVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Class represent rest controller which is responsible for administrator operations
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Gets all users except admins
     * @return all users except admins
     */
    @GetMapping("/users")
    public List<UserVM> getUsers() {
        List<UserVM> users = adminService.getUsers();
        return users;
    }

    /**
     * Gets the details about given user
     * @param userId user id
     * @return details about user
     */
    @GetMapping("/users/{userId}")
    public UserDetailVM getUserDetail(@PathVariable Long userId) {
        UserDetailVM user = adminService.getUserDetail(userId);
        return user;
    }

    /**
     * Updates username to given user
     * @param usernameUpdateVM new username
     * @param userId user id
     * @return message containing whether operation was processed
     */
    @PutMapping("/users/{userId}/updateUsername")
    public GenericResponse updateUserUsername(@Valid @RequestBody UsernameUpdateVM usernameUpdateVM, @PathVariable Long userId) {
        adminService.updateUserUsername(usernameUpdateVM, userId);
        return new GenericResponse("Username updated");
    }

    /**
     * Generates new password for user and sends it to his mail
     * @param userId user id
     * @return message containing whether operation was processed
     */
    @PutMapping("/users/{userId}/updatePassword")
    public GenericResponse updateUserPassword(@PathVariable Long userId) {
        adminService.updateUserPassword(userId);
        return new GenericResponse("New password send to mail box");
    }

    /**
     * Changes the value of user translator rights
     * @param booleanValVM new user rights value
     * @param userId user id
     * @return message containing whether operation was processed
     */
    @PutMapping("/users/{userId}/updateTranslator")
    public GenericResponse setTranslator(@Valid @RequestBody BooleanValVM booleanValVM, @PathVariable Long userId) {
        adminService.setTranslator(booleanValVM, userId);
        return new GenericResponse("Translator rights set");
    }

    /**
     * Removes user's managerial rights to his institution
     * @param userId user id
     * @return message containing whether operation was processed
     */
    @PutMapping("/users/{userId}/removeInstitution")
    public GenericResponse removeInstitution(@PathVariable Long userId) {
        adminService.removeInstitution(userId);
        return new GenericResponse("Institution removed");
    }

    /**
     * Changes if user is banned or not depending on given value
     * @param booleanValVM new ban value
     * @param userId user id
     * @return message containing whether operation was processed
     */
    @PutMapping("/users/{userId}/updateBan")
    public GenericResponse setBan(@Valid @RequestBody BooleanValVM booleanValVM, @PathVariable Long userId) {
        adminService.setBan(booleanValVM, userId);
        return new GenericResponse("Ban set");
    }
}
