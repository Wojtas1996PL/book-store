package mate.academy.service.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "SELECT id, title, author, isbn, price, description, cover_image "
            + "FROM books WHERE UPPER(id) LIKE UPPER(:id)",
            nativeQuery = true)
    Optional<Book> findBookById(Long id);

    @Query(value = "SELECT id, title, author, isbn, price, description, cover_image "
            + "FROM books WHERE UPPER(id) LIKE UPPER(:categoryId)",
            nativeQuery = true)
    List<Book> findAllByCategoryId(Long categoryId);
}
