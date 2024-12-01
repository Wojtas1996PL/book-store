package mate.academy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book savedBook = bookRepository.save(bookMapper
                .toModel(createBookRequestDto));
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        return bookMapper.toDto(
                bookRepository.findBookById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't find book with id: " + id)));
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
}
