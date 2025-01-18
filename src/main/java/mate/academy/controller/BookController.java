package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import mate.academy.service.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @PreAuthorize("USER")
    @Operation(summary = "Get list of all books")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @PreAuthorize("USER")
    @Operation(summary = "Get specific book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookMapper.toDto(bookService.findBookById(id));
    }

    @PreAuthorize("ADMIN")
    @Operation(summary = "Create a new book")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookMapper.toModel(bookDto));
    }

    @PreAuthorize("ADMIN")
    @Operation(summary = "Delete specific book by id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PreAuthorize("ADMIN")
    @Operation(summary = "Update specific book by id")
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book mappedBook = new Book();
        mappedBook.setId(book.getId());
        mappedBook.setTitle(book.getTitle());
        mappedBook.setAuthor(book.getAuthor());
        mappedBook.setIsbn(book.getIsbn());
        mappedBook.setDescription(book.getDescription());
        mappedBook.setCoverImage(book.getCoverImage());
        mappedBook.setPrice(book.getPrice());
        mappedBook.setDeleted(book.isDeleted());
        bookService.updateBook(id, mappedBook);
        return "The book: " + mappedBook.getTitle() + " has been updated.";
    }

    @Operation(summary = "Search books using parameters")
    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam BookSearchParametersDto searchParameters,
                                     Pageable pageable) {
        return bookService
                .search(searchParameters, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
