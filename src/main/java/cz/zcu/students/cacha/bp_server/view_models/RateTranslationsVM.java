package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.Language;
import cz.zcu.students.cacha.bp_server.domain.Translation;
import cz.zcu.students.cacha.bp_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * view model for rating translations
 */
@Data
@NoArgsConstructor
public class RateTranslationsVM {

    /**
     * translations
     */
    private List<RateTranslationVM> translations;
    /**
     * info about exhibit
     */
    private ExhibitVM exhibit;
    /**
     * language name
     */
    private String language;

    /**
     * Creates new instance based on given params
     * @param translations translations
     * @param exhibit exhibit
     * @param user user who rates
     * @param language language name
     */
    public RateTranslationsVM(Set<Translation> translations, Exhibit exhibit, User user, Language language) {
        // map to vm and sort by creation date
        this.translations = translations.stream().map(t -> new RateTranslationVM(t, user)).sorted(Comparator.comparing(RateTranslationVM::getCreatedAt).reversed())
                .collect(Collectors.toList());
        this.exhibit = new ExhibitVM(exhibit);
        this.language = language.getName();
    }
}
