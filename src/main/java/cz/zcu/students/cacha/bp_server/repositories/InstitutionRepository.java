package cz.zcu.students.cacha.bp_server.repositories;

import cz.zcu.students.cacha.bp_server.domain.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Class represent repository which is responsible for institutions db operations
 */
@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    /**
     * Gets institution by its name
     * @param name institutin name
     * @return found institution
     */
    Optional<Institution> findByName(String name);
}
