package cz.zcu.students.cacha.bp_server.repositories;

import cz.zcu.students.cacha.bp_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Class represent repository which is responsible for users db operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Gets user by username
     * @param userName username
     * @return found user
     */
    Optional<User> findByUsername(String userName);

    /**
     * Gets all non admin users
     * @return all non admin users
     */
    @Query(
            value = "select * from user u " +
                    "where 'ROLE_ADMIN' not in (" +
                    "select r.name from role r " +
                    "join users_roles ur on r.id = ur.role_id " +
                    "where ur.user_id = u.id) " +
                    "order by username",
            nativeQuery = true)
    List<User> getNonAdminUsers();
}
