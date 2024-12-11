package mate.academy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParametersDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.book.BookSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(Book book) {
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        return bookRepository.findBookById(id)
                .map(b -> {
                    b.setId(b.getId());
                    b.setAuthor(b.getAuthor());
                    b.setIsbn(b.getIsbn());
                    b.setDescription(b.getDescription());
                    b.setPrice(b.getPrice());
                    b.setCoverImage(b.getCoverImage());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> bookRepository.save(book));
    }

    @Override
    public List<Book> search(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParametersDto);
        return bookRepository.findAll(bookSpecification).stream().toList();
    }
}
