package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParametersDto;
import mate.academy.model.Book;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(Book book);

    List<Book> findAll(Pageable pageable);

    Book findBookById(Long id);

    void deleteById(Long id);

    Book updateBook(Long id, Book book);

    List<Book> search(BookSearchParametersDto bookSearchParametersDto, Pageable pageable);
}
