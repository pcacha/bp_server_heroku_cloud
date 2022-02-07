package cz.zcu.students.cacha.bp_server.view_models;

import cz.zcu.students.cacha.bp_server.domain.Exhibit;
import cz.zcu.students.cacha.bp_server.domain.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * view model containing exhibits and allowed languages
 */
@Data
@NoArgsConstructor
public class ExhibitsLanguagesVM {
    /**
     * allowed languages
     */
    private List<LanguageVM> languages;
    /**
     * exhibits
     */
    private List<ExhibitVM> exhibits;

    /**
     * Creates new instance with given languages and exhibits
     * @param languages allowed languages
     * @param exhibits exhibits
     */
    public ExhibitsLanguagesVM(Set<Language> languages, Set<Exhibit> exhibits) {
        // map to VM and sort languages by name
        this.languages = languages.stream().map(LanguageVM::new).sorted(Comparator.comparing(LanguageVM::getName)).collect(Collectors.toList());
        // map to VM and sort exhibits by name
        this.exhibits = exhibits.stream().map(ExhibitVM::new).sorted(Comparator.comparing(ExhibitVM::getName)).collect(Collectors.toList());
    }
}
