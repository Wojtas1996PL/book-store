package mate.academy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.model.Book;
import mate.academy.model.Category;
import mate.academy.service.repository.SpecificationProvider;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.book.BookSpecificationBuilder;
import mate.academy.service.repository.book.BookSpecificationProviderManager;
import mate.academy.service.repository.book.spec.AuthorSpecificationProvider;
import mate.academy.service.repository.book.spec.TitleSpecificationProvider;
import mate.academy.service.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
@AutoConfigureTestDatabase
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify that method findBookById works")
    public void findById_CorrectBook_ReturnsOptionalOfBook() {
        long id = 1L;
        Book harryPotterExpected = new Book();
        harryPotterExpected.setId(id);
        harryPotterExpected.setTitle("Harry Potter");
        harryPotterExpected.setAuthor("J.K. Rowling");
        harryPotterExpected.setIsbn("1234");
        harryPotterExpected.setPrice(BigDecimal.valueOf(120.99));
        bookRepository.save(harryPotterExpected);

        Book harryPotterActual = bookRepository.findBookById(id).get();

        assertThat(harryPotterActual).isEqualTo(harryPotterExpected);
        assertNotNull(harryPotterActual);
    }

    @Test
    @DisplayName("Verify that method findAllByCategoryId works")
    public void findAll_CorrectCategoryId_ReturnsBookList() {
        Category scienceFiction = new Category();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science fiction");
        scienceFiction.setDescription("Science fiction books");
        categoryRepository.save(scienceFiction);

        Set<Category> categories = new HashSet<>();
        categories.add(scienceFiction);

        Book harryPotter = new Book();
        harryPotter.setId(1L);
        harryPotter.setTitle("Harry Potter");
        harryPotter.setAuthor("J.K. Rowling");
        harryPotter.setIsbn("1234");
        harryPotter.setPrice(BigDecimal.valueOf(120.99));
        harryPotter.setCategories(categories);
        bookRepository.save(harryPotter);

        Book gameOfThrones = new Book();
        gameOfThrones.setId(2L);
        gameOfThrones.setTitle("Game of Thrones");
        gameOfThrones.setAuthor("George Martin");
        gameOfThrones.setIsbn("9897");
        gameOfThrones.setPrice(BigDecimal.valueOf(12.99));
        gameOfThrones.setCategories(categories);
        bookRepository.save(gameOfThrones);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Book> expectedBooks = List.of(harryPotter, gameOfThrones);

        List<Book> actualBooks = bookRepository.findAllByCategoryId(1L, pageable);

        assertThat(actualBooks).isEqualTo(expectedBooks);
        assertNotNull(actualBooks);
    }

    @Test
    @DisplayName("Verify that method findAll with one param works")
    public void findAll_Pageable_ReturnsPageOfBooks() {
        Book harryPotter = new Book();
        harryPotter.setId(1L);
        harryPotter.setTitle("Harry Potter");
        harryPotter.setAuthor("J.K. Rowling");
        harryPotter.setIsbn("1234");
        harryPotter.setPrice(BigDecimal.valueOf(120.99));
        bookRepository.save(harryPotter);

        Book gameOfThrones = new Book();
        gameOfThrones.setId(2L);
        gameOfThrones.setTitle("Game of Thrones");
        gameOfThrones.setAuthor("George Martin");
        gameOfThrones.setIsbn("9897");
        gameOfThrones.setPrice(BigDecimal.valueOf(12.99));
        bookRepository.save(gameOfThrones);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Book> books = List.of(harryPotter, gameOfThrones);
        Page<Book> expectedBooks = new PageImpl<>(books, pageable, books.size());

        Page<Book> actualBooks = bookRepository.findAll(pageable);

        assertThat(actualBooks).isEqualTo(expectedBooks);
        assertNotNull(actualBooks);
    }

    @Test
    @DisplayName("Verify that method findAll with two params works")
    public void findAll_BookSpecificationAndPageable_ReturnsPageOfBooks() {
        Book harryPotter = new Book();
        harryPotter.setId(1L);
        harryPotter.setTitle("Harry Potter");
        harryPotter.setAuthor("J.K. Rowling");
        harryPotter.setIsbn("1234");
        harryPotter.setPrice(BigDecimal.valueOf(120.99));
        bookRepository.save(harryPotter);

        Book gameOfThrones = new Book();
        gameOfThrones.setId(2L);
        gameOfThrones.setTitle("Game of Thrones");
        gameOfThrones.setAuthor("George Martin");
        gameOfThrones.setIsbn("9897");
        gameOfThrones.setPrice(BigDecimal.valueOf(12.99));
        bookRepository.save(gameOfThrones);

        String[] titles = {"Harry Potter", "Game of Thrones"};
        String[] authors = {"J.K. Rowling", "George Martin"};

        BookSearchParametersDto bookSearchParametersDto = new
                BookSearchParametersDto(titles, authors);
        SpecificationProvider<Book> title = new TitleSpecificationProvider();
        SpecificationProvider<Book> author = new AuthorSpecificationProvider();
        List<SpecificationProvider<Book>> providers = new ArrayList<>();
        providers.add(title);
        providers.add(author);
        BookSpecificationProviderManager bookSpecificationProviderManager = new
                BookSpecificationProviderManager(providers);
        BookSpecificationBuilder bookSpecificationBuilder = new
                BookSpecificationBuilder(bookSpecificationProviderManager);
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParametersDto);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<Book> books = List.of(harryPotter, gameOfThrones);
        Page<Book> expectedBooks = new PageImpl<>(books, pageable, books.size());

        Page<Book> actualBooks = bookRepository.findAll(bookSpecification, pageable);

        assertThat(actualBooks).hasSize(2);
        assertThat(actualBooks).isEqualTo(expectedBooks);
        assertNotNull(actualBooks);
    }
}
