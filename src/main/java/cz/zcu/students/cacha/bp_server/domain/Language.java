package cz.zcu.students.cacha.bp_server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents language
 */
@Entity
@Data
@NoArgsConstructor
public class Language {

    /**
     * id - primary key
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * english name of language
     */
    @Column(length = 100)
    private String name;

    /**
     * two letters long language code
     */
    @Column(length = 2)
    private String code;

    /**
     * all institutions that support language
     */
    @ManyToMany(mappedBy = "languages", fetch= FetchType.LAZY)
    private Set<Institution> institutions;

    /**
     * Creates new instance with give params
     * @param name language name
     * @param code language code
     */
    public Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Gets whether two instances represents the same language
     * @param o comparing language
     * @return whether two instances represents the same language
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;
        Language language = (Language) o;
        return id.equals(language.id);
    }

    /**
     * Gets the hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
