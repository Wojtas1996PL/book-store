package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/books")
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get list of all books")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable)
                .stream()
                .toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get specific book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete specific book by id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update specific book by id")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody CreateBookRequestDto book) {
        CreateBookRequestDto mappedBook = new CreateBookRequestDto();
        mappedBook.setTitle(book.getTitle());
        mappedBook.setAuthor(book.getAuthor());
        mappedBook.setIsbn(book.getIsbn());
        mappedBook.setDescription(book.getDescription());
        mappedBook.setCoverImage(book.getCoverImage());
        mappedBook.setPrice(book.getPrice());
        return bookService.updateBook(id, mappedBook);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Search books using parameters")
    @GetMapping("/search")
    public List<BookDto> searchBooks(@ModelAttribute BookSearchParametersDto searchParameters,
                                     Pageable pageable) {
        return bookService
                .search(searchParameters, pageable)
                .stream()
                .toList();
    }
}
