package cz.zcu.students.cacha.bp_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.bp_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * view model of user
 */
@Data
@NoArgsConstructor
public class UserVM {
    /**
     * user id
     */
    private Long userId;
    /**
     * username
     */
    private String username;
    /**
     * email
     */
    private String email;
    /**
     * registration date
     */
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdAt;
    /**
     * whether user is banned
     */
    private Boolean isBanned;

    /**
     * Creates new instance based on given user
     * @param user user
     */
    public UserVM(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.isBanned = user.getBanned();
    }
}