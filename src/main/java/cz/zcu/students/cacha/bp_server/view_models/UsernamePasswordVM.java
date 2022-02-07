package cz.zcu.students.cacha.bp_server.view_models;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * view model for username and password input
 */
@Data
public class UsernamePasswordVM {
    /**
     * username
     */
    @NotNull(message = "Username can not be blank")
    private String username;
    /**
     * password
     */
    @NotNull(message = "Password can not be blank")
    private String password;
}
