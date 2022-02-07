package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * view model for making new translation
 */
@Data
@NoArgsConstructor
public class NewTranslationVM {
    /**
     * exhibit name
     */
    private String exhibitName;
    /**
     * language name
     */
    private String languageName;
    /**
     * text of info label
     */
    private String infoLabelText;
    /**
     * name of image of info label
     */
    private String infoLabel;
    /**
     * translated text
     */
    private String text;

    /**
     * Creates new instance based on given parameters
     * @param exhibit exhibit
     * @param text translated text
     * @param languageName language name
     */
    public NewTranslationVM(Exhibit exhibit, String text, String languageName) {
        exhibitName = exhibit.getName();
        infoLabelText = exhibit.getInfoLabelText();
        infoLabel = exhibit.getInfoLabel();
        this.text = text;
        this.languageName = languageName;
    }
}
