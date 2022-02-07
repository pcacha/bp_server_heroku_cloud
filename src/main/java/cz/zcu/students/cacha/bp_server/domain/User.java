package cz.zcu.students.cacha.bp_server.domain;

import cz.zcu.students.cacha.bp_server.validators.UniqueUsername;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.*;

/**
 * Class that represents user of translation system
 */
@Entity
@Data
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * id - primary key
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * unique username
     */
    @NotNull(message = "Username can not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 to 30 letters long")
    @UniqueUsername
    @Column(length = 30)
    private String username;

    /**
     * hashed password
     */
    @NotNull(message = "Password can not be blank")
    @Size(min = 8, max = 50, message = "Password must be between 8 to 50 letters long")
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message="Password must contain a lowercase and an uppercase letter and a number")
    private String password;

    /**
     * email address
     */
    @NotNull(message = "E-mail can not be blank")
    @Email(message = "Bad e-mail format")
    @Size(min = 1, max = 50, message = "E-mail must be maximally 50 letters long")
    @Column(length = 50)
    private String email;

    /**
     * registration date
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * indicates whether user is banned
     */
    private Boolean banned;

    /**
     * all translation written by user
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Translation> translations;

    /**
     * institution that is managed by user
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private Institution institution;

    /**
     * roles that represents user's rights
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * all translations that user liked
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "translations_likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "translation_id", referencedColumnName = "id"))
    private Set<Translation> likedTranslations = new HashSet<>();

    /**
     * Sets default values for newly crated user
     */
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        banned = false;
    }

    /**
     * Gets all authorities represented by roles that user has
     * @return all user's authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // map roles to authorities
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    /**
     * indicates whether is account expired
     * @return whether is account expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * indicates if the account is locked
     * @return if the account is locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * gets if user's credentials are expired
     * @return if user's credentials are expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * gets whether user is banned
     * @return whether user is banned
     */
    @Override
    public boolean isEnabled() {
        return !banned;
    }

    /**
     * Gets whether user is translator
     * @return whether user is translator
     */
    public boolean isTranslator() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_TRANSLATOR);
    }

    /**
     * Gets whether user manages an institution
     * @return whether user manages an institution
     */
    public boolean isInstitutionOwner() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_INSTITUTION_OWNER);
    }

    /**
     * Gets whether user is an administrator
     * @return  whether user is an administrator
     */
    public boolean isAdmin() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_ADMIN);
    }

    /**
     * Gets whether two instances represents the same user
     * @param o comparing user
     * @return whether two instances represents the same user
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
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
