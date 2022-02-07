package cz.zcu.students.cacha.bp_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * view model for updating username
 */
@Data
@NoArgsConstructor
public class UsernameUpdateVM {
    /**
     * username
     */
    @NotNull(message = "Username can not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 to 30 letters long")
    private String username;
}