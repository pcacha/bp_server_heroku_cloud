package cz.zcu.students.cacha.bp_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.bp_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * view model for user details
 */
@Data
@NoArgsConstructor
public class UserDetailVM {
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
     * user registration name
     */
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdAt;
    /**
     * whether user is banned
     */
    private Boolean isBanned;
    /**
     * whether user is translator
     */
    private Boolean isTranslator;
    /**
     * whether user is owner of an institution
     */
    private Boolean isInstitutionOwner;
    /**
     * name of an institution
     */
    private String institutionName;

    /**
     * creates new instance based on given user
     * @param user user
     */
    public UserDetailVM(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.isBanned = user.getBanned();
        this.isTranslator = user.isTranslator();
        this.isInstitutionOwner = user.isInstitutionOwner();

        if(user.getInstitution() != null) {
            this.institutionName = user.getInstitution().getName();
        }
    }
}
