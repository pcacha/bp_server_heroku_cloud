package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.validators.UniqueUsernameExclPrincipal;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * view model for updating user
 */
@Data
public class UserUpdateVM {
    /**
     * username
     */
    @NotNull(message = "Username can not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 to 30 letters long")
    @UniqueUsernameExclPrincipal
    private String username;

    /**
     * email
     */
    @NotNull(message = "E-mail can not be blank")
    @Email(message = "Bad e-mail format")
    @Size(min = 1, max = 50, message = "E-mail must be maximally 50 letters long")
    private String email;
}
