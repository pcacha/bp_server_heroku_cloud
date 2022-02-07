package cz.zcu.students.cacha.bp_server.repositories;

import cz.zcu.students.cacha.bp_server.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Class represent repository which is responsible for languages db operations
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    /**
     * Gets all languages ordered by name
     * @return all languages ordered
     */
    Set<Language> findAllByOrderByName();

    /**
     * Finds language by its code
     * @param code language code
     * @return found language
     */
    Optional<Language> findByCode(String code);
}
