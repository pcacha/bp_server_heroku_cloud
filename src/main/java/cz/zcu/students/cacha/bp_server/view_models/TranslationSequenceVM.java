package cz.zcu.students.cacha.bp_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.Language;
import cz.zcu.students.cacha.bp_server.domain.Translation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * view model for translation sequence
 */
@Data
@NoArgsConstructor
public class TranslationSequenceVM {

    /**
     * exhibit id
     */
    private Long exhibitId;
    /**
     * language id
     */
    private Long languageId;
    /**
     * institution name
     */
    private String institutionName;
    /**
     * exhibit name
     */
    private String exhibitName;
    /**
     * language name
     */
    private String language;
    /**
     * name of image of exhibit
     */
    private String exhibitImage;
    /**
     * the date of last activity in sequence
     */
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date latestTranslationCreatedAt;

    /**
     * Creates new instance based on given translation
     * @param translation translation
     */
    public TranslationSequenceVM(Translation translation) {
        Exhibit ex = translation.getExhibit();
        Language lang = translation.getLanguage();

        exhibitId = ex.getId();
        languageId = lang.getId();
        institutionName = ex.getInstitution().getName();
        exhibitName = ex.getName();
        language = lang.getName();
        exhibitImage = ex.getImage();
        latestTranslationCreatedAt = translation.getCreatedAt();
    }
}
