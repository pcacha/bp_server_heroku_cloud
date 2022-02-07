package cz.zcu.students.cacha.bp_server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents translation attached to exhibit and langugae
 */
@Entity
@Data
@NoArgsConstructor
public class Translation {

    /**
     * id - primary key
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * translated text
     */
    @NotNull(message = "Translation can not be empty")
    @Size(min = 1, max = 25000, message = "Translation must be between 1 to 25000 letters long")
    @Column(length = 25000, columnDefinition="TEXT")
    private String text;

    /**
     * indicates whether translation is set as official
     */
    private Boolean isOfficial;

    /**
     * autor of translation
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private User author;

    /**
     * language of translation
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private Language language;

    /**
     * exhibit for which the translation was made for
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private Exhibit exhibit;

    /**
     * users that liked this translation
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "translations_likes",
            joinColumns = @JoinColumn(name = "translation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> likers;

    /**
     * date when the translation was created
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * sets default params for new translations
     */
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        isOfficial = false;
    }

    /**
     * Gets whether two instances represents the same translation
     * @param o comparing translation
     * @return whether two instances represents the same translation
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Translation)) return false;
        Translation that = (Translation) o;
        return id.equals(that.id);
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
