package mate.academy.service.repository.user;

import java.util.Optional;
import mate.academy.model.Book;
import mate.academy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(name = "SELECT * FROM users WHERE UPPER(email) LIKE UPPER(:email)",
            nativeQuery = true)
    Optional<Book> findUserByEmail(String email);
}
