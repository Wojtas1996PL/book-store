package mate.academy.service;

import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto createBookRequestDto);

    List<BookDto> search(BookSearchParametersDto bookSearchParametersDto,
                      Pageable pageable);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id,
                                                               Pageable pageable);
}
