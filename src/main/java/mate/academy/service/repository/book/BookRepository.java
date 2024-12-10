package mate.academy.service.repository.book;

import java.util.Optional;
import mate.academy.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(name = "SELECT * FROM books WHERE UPPER(id) LIKE UPPER(:id)",
            nativeQuery = true)
    Optional<Book> findBookById(Long id);
}
