package cz.zcu.students.cacha.bp_server.domain;

import cz.zcu.students.cacha.bp_server.validators.PngJpgFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static cz.zcu.students.cacha.bp_server.assets_store_config.WebConfiguration.DEFAULT_EXHIBIT_IMAGE;

/**
 * Represents an exhibit of a cultural institution
 */
@Entity
@Data
@NoArgsConstructor
public class Exhibit {

    /**
     * id - primary key
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * name of exhibit
     */
    @NotNull(message = "Name can not be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 to 100 letters long")
    @Column(length = 100)
    private String name;

    /**
     * name of image of exhibit in fs
     */
    @Column(length = 100)
    private String image = DEFAULT_EXHIBIT_IMAGE;

    /**
     * base64 encoded image of exhibit
     */
    @Transient
    @PngJpgFile
    private String encodedImage;

    /**
     * text of info label
     */
    @NotNull(message = "Info label text can not be blank")
    @Size(max = 25000, message = "The text of an information label must be maximally 25000 letters long")
    @Column(length = 25000, columnDefinition="TEXT")
    private String infoLabelText;

    /**
     * name of image of info label in fs
     */
    @Column(length = 100)
    private String infoLabel;

    /**
     * base64 encoded info label
     */
    @Transient
    @PngJpgFile
    @NotNull(message = "Informational label of an exhibit can not be blank")
    private String encodedInfoLabel;

    /**
     * building where is exhibit located
     */
    @NotNull(message = "Building can not be blank")
    @Size(max = 50, message = "Building must be maximally 50 letters long")
    @Column(length = 50)
    private String building;

    /**
     * room where is exhibit located
     */
    @NotNull(message = "Room can not be blank")
    @Size(max = 50, message = "Room must be maximally 50 letters long")
    @Column(length = 50)
    private String room;

    /**
     * show-case where is exhibit located
     */
    @NotNull(message = "Show-case can not be blank")
    @Size(max = 50, message = "Show-case must be maximally 50 letters long")
    @Column(length = 50)
    private String showcase;

    /**
     * all translations matching to exhibit
     */
    @OneToMany(mappedBy = "exhibit", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private Set<Translation> translations;

    /**
     * institution owning exhibit
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private Institution institution;

    /**
     * register date of exhibit
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * saves registration date on new exhibit save
     */
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    /**
     * Defines whether two exhibit instances are the same exhibit
     * @param o comparing object
     * @return whether two exhibit instances are the same exhibit
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibit)) return false;
        Exhibit exhibit = (Exhibit) o;
        return id.equals(exhibit.id);
    }

    /**
     * Gets the hash code of exhibit
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
