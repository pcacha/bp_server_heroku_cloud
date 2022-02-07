package cz.zcu.students.cacha.bp_server.repositories;

import cz.zcu.students.cacha.bp_server.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Class represent repository which is responsible for roles db operations
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Gets role by its name
     * @param name role name
     * @return found role
     */
    Optional<Role> findByName(String name);
}
