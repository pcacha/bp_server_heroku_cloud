package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.domain.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Allowed languages view model
 */
@Data
@NoArgsConstructor
public class AllowedLanguagesVM {

    /**
     * possible languages to add
     */
    private List<LanguageVM> possibleLanguages;
    /**
     * already chosen languages
     */
    private List<LanguageVM> chosenLanguages;

    /**
     * Creates new instance with given possible languages and chosen languages
     * @param possibleLanguages possible languages
     * @param chosenLanguages chosen languages
     */
    public AllowedLanguagesVM(Set<Language> possibleLanguages, Set<Language> chosenLanguages) {
        this.possibleLanguages = possibleLanguages.stream().map(LanguageVM::new).sorted(Comparator.comparing(LanguageVM::getName)).collect(Collectors.toList());
        this.chosenLanguages = chosenLanguages.stream().map(LanguageVM::new).sorted(Comparator.comparing(LanguageVM::getName)).collect(Collectors.toList());
    }
}
