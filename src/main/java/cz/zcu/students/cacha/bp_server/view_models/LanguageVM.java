package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.domain.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * language view model
 */
@Data
@NoArgsConstructor
public class LanguageVM {
    /**
     * language id
     */
    private Long languageId;
    /**
     * language name
     */
    private String name;
    /**
     * two letters long language code
     */
    private String code;

    /**
     * Creates new instance based on given language
     * @param language language
     */
    public LanguageVM(Language language) {
        languageId = language.getId();
        name = language.getName();
        code = language.getCode();
    }
}
