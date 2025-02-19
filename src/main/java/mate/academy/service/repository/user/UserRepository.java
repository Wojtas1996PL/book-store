package mate.academy.service.repository.user;

import java.util.Optional;
import mate.academy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.roles "
            + "WHERE UPPER(u.email) = UPPER(:email)")
    Optional<User> findUserByEmail(String email);
}
