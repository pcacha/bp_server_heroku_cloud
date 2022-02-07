package cz.zcu.students.cacha.bp_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.bp_server.domain.Translation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * view model for translation
 */
@Data
@NoArgsConstructor
public class TranslationVM {

    /**
     * translation id
     */
    private Long translationId;
    /**
     * institution id
     */
    private Long institutionId;
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
     * translation creation date
     */
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdAt;
    /**
     * count of likes of translation
     */
    private Integer likesCount;

    /**
     * Cretes new instance based on given translation
     * @param translation translation
     */
    public TranslationVM(Translation translation) {
        translationId = translation.getId();
        institutionId = translation.getExhibit().getInstitution().getId();
        authorUsername = translation.getAuthor().getUsername();
        translatedText = translation.getText();
        isOfficial = translation.getIsOfficial();
        createdAt = translation.getCreatedAt();
        likesCount = translation.getLikers().size();
    }
}
