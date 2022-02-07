package cz.zcu.students.cacha.bp_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.bp_server.domain.Translation;
import cz.zcu.students.cacha.bp_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * view model for rating translation
 */
@Data
@NoArgsConstructor
public class RateTranslationVM {

    /**
     * translation id
     */
    private Long translationId;
    /**
     * author username
     */
    private String authorUsername;
    /**
     * translated text
     */
    private String translatedText;
    /**
     * whether translation is official
     */
    private Boolean isOfficial;
    /**
     * translation created date
     */
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdAt;
    /**
     * count of likes for translation
     */
    private Integer likesCount;
    /**
     * specifies whether user liked this translation
     */
    private Boolean liked;

    /**
     * Creates new instance based on given params
     * @param translation translation
     * @param user user who rates
     */
    public RateTranslationVM(Translation translation, User user) {
        translationId = translation.getId();
        authorUsername = translation.getAuthor().getUsername();
        translatedText = translation.getText();
        isOfficial = translation.getIsOfficial();
        createdAt = translation.getCreatedAt();
        likesCount = translation.getLikers().size();
        liked = translation.getLikers().stream().anyMatch(u -> u.getId().equals(user.getId()));
    }
}
