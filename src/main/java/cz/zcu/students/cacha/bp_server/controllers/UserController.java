package cz.zcu.students.cacha.bp_server.controllers;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.responses.GenericResponse;
import cz.zcu.students.cacha.bp_server.responses.JWTLoginSuccessResponse;
import cz.zcu.students.cacha.bp_server.services.UserService;
import cz.zcu.students.cacha.bp_server.shared.CurrentUser;
import cz.zcu.students.cacha.bp_server.view_models.PasswordUpdateVM;
import cz.zcu.students.cacha.bp_server.view_models.UsernamePasswordVM;
import cz.zcu.students.cacha.bp_server.view_models.UserUpdateVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Class represent rest controller which is responsible for user operations
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registeres a new user in the system
     * @param user new user
     * @return message containing whether operation was processed
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse register(@Valid @RequestBody User user) {
        userService.save(user);
        return new GenericResponse("User registered");
    }

    /**
     * Logs user in based on given credentials
     * @param usernamePasswordVM username and password
     * @return jwt for authentication
     */
    @PostMapping("/login")
    public JWTLoginSuccessResponse login(@Valid @RequestBody UsernamePasswordVM usernamePasswordVM) {
        JWTLoginSuccessResponse response = userService.login(usernamePasswordVM);
        return response;
    }

    /**
     * Generates new token for authenticated user to avoid expiration
     * @return jwt for authentication
     */
    @GetMapping("/token")
    public JWTLoginSuccessResponse token() {
        JWTLoginSuccessResponse response = userService.getFreshToken();
        return response;
    }

    /**
     * Updates user's personal information
     * @param userUpdateVM updated username and email
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PutMapping("/updateUser")
    public GenericResponse updateUser(@Valid @RequestBody UserUpdateVM userUpdateVM, @CurrentUser User user) {
        userService.updateUser(user, userUpdateVM.getUsername(), userUpdateVM.getEmail());
        return new GenericResponse("User updated");
    }

    /**
     * Changes user's password
     * @param passwordUpdateVM changed password
     * @param user logged in user
     * @return message containing whether operation was processed
     */
    @PutMapping("/updatePassword")
    public GenericResponse updatePassword(@Valid @RequestBody PasswordUpdateVM passwordUpdateVM, @CurrentUser User user) {
        userService.updatePassword(user, passwordUpdateVM.getPassword());
        return new GenericResponse("Password updated");
    }
}

