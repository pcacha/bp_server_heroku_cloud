package cz.zcu.students.cacha.bp_server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents user's role
 */
@Entity
@Data
@NoArgsConstructor
public class Role {

    /**
     * primary key - id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * name of role
     */
    @Column(length = 40)
    private String name;

    /**
     * all users with role
     */
    @ManyToMany(mappedBy = "roles", fetch= FetchType.LAZY)
    private Set<User> users;

    /**
     * creates new role with given params
     * @param name role name
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Gets whether two instances represents the same role
     * @param o comparing role
     * @return whether two instances represents the same role
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return id.equals(role.id);
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
