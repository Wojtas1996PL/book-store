package mate.academy.service.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "SELECT b "
            + "FROM Book b LEFT JOIN FETCH "
            + "b.categories "
            + "WHERE b.id=:id")
    Optional<Book> findBookById(@Param("id") Long id);

    @Query(value = "SELECT b.id, b.title, b.author, b.isbn, b.price, b.description, "
            + "b.cover_image, b.is_deleted "
            + "FROM books b "
            + "JOIN book_categories bc ON b.id = bc.book_id "
            + "JOIN categories c ON c.id = bc.category_id "
            + "WHERE bc.category_id = :categoryId",
            countQuery = "SELECT count(b.id) FROM books b "
                    + "JOIN book_categories bc ON b.id = bc.book_id "
                    + "JOIN categories c ON c.id = bc.category_id "
                    + "WHERE bc.category_id = :categoryId",
            nativeQuery = true)
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Override
    @EntityGraph(value = "Book.categories", type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "Book.categories", type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findAll(Specification<Book> bookSpecification, Pageable pageable);
}
