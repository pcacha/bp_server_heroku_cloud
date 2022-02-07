package cz.zcu.students.cacha.bp_server.security;

import lombok.Data;

/**
 * Response returned on unsuccessful login
 */
@Data
public class InvalidLoginResponse {
    /**
     * username error
     */
    private String username;
    /**
     * password error
     */
    private String password;

    /**
     * Creates new instance with default values
     */
    public InvalidLoginResponse() {
        this.username = "Invalid name";
        this.password = "Invalid password";
    }
}
