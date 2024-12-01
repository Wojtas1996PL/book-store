package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.model.Book;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    void deleteById(Long id);

    Book updateBook(Long id, Book book);
}
