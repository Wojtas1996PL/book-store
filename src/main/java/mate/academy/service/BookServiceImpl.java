package mate.academy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(book)));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        return bookMapper.toDto(bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto createBookRequestDto) {
        Book updatedBook = bookRepository.findBookById(id)
                .map(b -> {
                    b.setTitle(createBookRequestDto.getTitle());
                    b.setAuthor(createBookRequestDto.getAuthor());
                    b.setIsbn(createBookRequestDto.getIsbn());
                    b.setDescription(createBookRequestDto.getDescription());
                    b.setPrice(createBookRequestDto.getPrice());
                    b.setCoverImage(createBookRequestDto.getCoverImage());
                    return b;
                })
                .orElseThrow(() -> new RuntimeException("Could not update book by id: " + id));
        bookRepository.save(updatedBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto,
                             Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParametersDto);
        return bookRepository.findAll(bookSpecification,
                pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id,
                                                                      Pageable pageable) {
        return bookRepository
                .findAllByCategoryId(id, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
