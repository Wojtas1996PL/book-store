package mate.academy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Test
    @DisplayName("Verify that method save works")
    public void save_CorrectBook_ReturnsBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Harry Potter");
        createBookRequestDto.setIsbn("1234");
        createBookRequestDto.setPrice(BigDecimal.valueOf(120.99));
        createBookRequestDto.setAuthor("J.K. Rowling");
        createBookRequestDto.setCoverImage("Image");
        createBookRequestDto.setDescription("Book about magic");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setIsbn("1234");
        book.setPrice(BigDecimal.valueOf(120.99));
        book.setAuthor("J.K. Rowling");
        book.setCoverImage("Image");
        book.setDescription("Book about magic");

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Harry Potter");
        bookDto.setIsbn("1234");
        bookDto.setPrice(BigDecimal.valueOf(120.99));
        bookDto.setAuthor("J.K. Rowling");
        bookDto.setCoverImage("Image");
        bookDto.setDescription("Book about magic");

        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);

        BookDto savedBookDto = bookService.save(createBookRequestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookMapper, times(1)).toEntity(createBookRequestDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that findAll method works")
    public void findAll_CorrectBooks_ReturnsBookList() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Harry Potter");
        book1.setIsbn("1234");
        book1.setPrice(BigDecimal.valueOf(120.99));
        book1.setAuthor("J.K. Rowling");
        book1.setCoverImage("Image");
        book1.setDescription("Book about magic");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Game of Thrones");
        book2.setIsbn("1234567");
        book2.setPrice(BigDecimal.valueOf(120.99));
        book2.setAuthor("George Martin");
        book2.setCoverImage("Image Game");
        book2.setDescription("Book about power");

        BookDto bookDto1 = new BookDto();
        bookDto1.setTitle("Harry Potter");
        bookDto1.setIsbn("1234");
        bookDto1.setPrice(BigDecimal.valueOf(120.99));
        bookDto1.setAuthor("J.K. Rowling");
        bookDto1.setCoverImage("Image");
        bookDto1.setDescription("Book about magic");

        BookDto bookDto2 = new BookDto();
        bookDto2.setTitle("Game of Thrones");
        bookDto2.setIsbn("1234567");
        bookDto2.setPrice(BigDecimal.valueOf(120.99));
        bookDto2.setAuthor("George Martin");
        bookDto2.setCoverImage("Image Game");
        bookDto2.setDescription("Book about power");

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        List<BookDto> bookDtoList = bookService.findAll(pageable);

        assertThat(bookDtoList).hasSize(2);
        assertThat(bookDtoList.get(0)).isEqualTo(bookDto1);
        assertThat(bookDtoList.get(1)).isEqualTo(bookDto2);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book1);
        verify(bookMapper, times(1)).toDto(book2);
    }

    @Test
    @DisplayName("Verify that method findById works")
    public void findById_BookWithCorrectId_ReturnsBookDto() {
        long id = 1L;

        Book book = new Book();
        book.setId(id);
        book.setTitle("Harry Potter");
        book.setIsbn("1234");
        book.setPrice(BigDecimal.valueOf(120.99));
        book.setAuthor("J.K. Rowling");
        book.setCoverImage("Image");
        book.setDescription("Book about magic");

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Harry Potter");
        bookDto.setIsbn("1234");
        bookDto.setPrice(BigDecimal.valueOf(120.99));
        bookDto.setAuthor("J.K. Rowling");
        bookDto.setCoverImage("Image");
        bookDto.setDescription("Book about magic");

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findBookById(id)).thenReturn(Optional.of(book));

        BookDto foundBookDto = bookService.findBookById(id);

        assertThat(foundBookDto).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findBookById(id);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Verify that method findById throws EntityNotFoundException when id is incorrect")
    public void findById_BookWithIncorrectId_ReturnsBookDto() {
        long id = 1L;

        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(id));
    }

    @Test
    @DisplayName("Verify that method deleteById works")
    public void deleteById_Book_ReturnsNothing() {
        long id = 1L;

        bookService.deleteById(id);

        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Verify that method update book works")
    public void update_CorrectBook_ReturnsUpdatedBookDto() {
        long id = 1L;

        Book book = new Book();
        book.setId(id);
        book.setTitle("Harry Potter");
        book.setIsbn("1234");
        book.setPrice(BigDecimal.valueOf(120.99));
        book.setAuthor("J.K. Rowling");
        book.setCoverImage("Image");
        book.setDescription("Book about magic");

        CreateBookRequestDto requestBook = new CreateBookRequestDto();
        requestBook.setTitle("Harry");
        requestBook.setIsbn("12345");
        requestBook.setPrice(BigDecimal.valueOf(10.99));
        requestBook.setAuthor("Rowling");
        requestBook.setCoverImage("Ima");
        requestBook.setDescription("Magic");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Harry");
        updatedBook.setIsbn("12345");
        updatedBook.setPrice(BigDecimal.valueOf(10.99));
        updatedBook.setAuthor("Rowling");
        updatedBook.setCoverImage("Ima");
        updatedBook.setDescription("Magic");

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setTitle("Harry");
        updatedBookDto.setIsbn("12345");
        updatedBookDto.setPrice(BigDecimal.valueOf(10.99));
        updatedBookDto.setAuthor("Rowling");
        updatedBookDto.setCoverImage("Ima");
        updatedBookDto.setDescription("Magic");

        when(bookRepository.findBookById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        BookDto updatedBookSaved = bookService.updateBook(id, requestBook);

        assertThat(updatedBookSaved).isEqualTo(updatedBookDto);

        verify(bookRepository, times(1)).findBookById(id);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    @DisplayName("Verify that method search works")
    public void search_CorrectSearchParameters_ReturnsBookDtoList() {
        Book harryPotter = new Book();
        harryPotter.setId(1L);
        harryPotter.setAuthor("J.K. Rowling");
        harryPotter.setTitle("Harry Potter");
        harryPotter.setIsbn("12");
        harryPotter.setPrice(BigDecimal.valueOf(12.99));

        BookDto harryPotterDto = new BookDto();
        harryPotterDto.setAuthor("J.K. Rowling");
        harryPotterDto.setTitle("Harry Potter");
        harryPotterDto.setIsbn("12");
        harryPotterDto.setPrice(BigDecimal.valueOf(12.99));

        Book gameOfThrones = new Book();
        gameOfThrones.setId(2L);
        gameOfThrones.setAuthor("George Martin");
        gameOfThrones.setTitle("Game of Thrones");
        gameOfThrones.setIsbn("1234");
        gameOfThrones.setPrice(BigDecimal.valueOf(112.99));

        BookDto gameOfThronesDto = new BookDto();
        gameOfThronesDto.setAuthor("George Martin");
        gameOfThronesDto.setTitle("Game of Thrones");
        gameOfThronesDto.setIsbn("1234");
        gameOfThronesDto.setPrice(BigDecimal.valueOf(112.99));

        String[] titles = {"Harry Potter", "Game of Thrones"};
        String[] authors = {"J.K. Rowling, George Martin"};

        BookSearchParametersDto bookSearchParametersDto = new
                BookSearchParametersDto(titles, authors);
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(harryPotter, gameOfThrones);
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParametersDto);
        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(books.get(0))).thenReturn(harryPotterDto);
        when(bookMapper.toDto(books.get(1))).thenReturn(gameOfThronesDto);

        List<BookDto> searchedBooksDto = bookService.search(bookSearchParametersDto, pageable);

        assertNotNull(searchedBooksDto);
        assertThat(searchedBooksDto).hasSize(2);
        assertThat(harryPotterDto).isEqualTo(searchedBooksDto.get(0));
        assertThat(gameOfThronesDto).isEqualTo(searchedBooksDto.get(1));

        verify(bookRepository, times(1)).findAll(bookSpecification, pageable);
        verify(bookMapper, times(1)).toDto(books.get(0));
        verify(bookMapper, times(1)).toDto(books.get(1));
    }

    @Test
    @DisplayName("Verify that method findBooksByCategoryId works")
    public void findByCategoryId_CorrectBooks_ReturnsListOfBookWithNoCategoryDto() {
        Book harryPotter = new Book();
        harryPotter.setId(1L);
        harryPotter.setAuthor("J.K. Rowling");
        harryPotter.setTitle("Harry Potter");
        harryPotter.setIsbn("12");
        harryPotter.setPrice(BigDecimal.valueOf(12.99));

        BookDtoWithoutCategoryIds harryPotterDto = new BookDtoWithoutCategoryIds();
        harryPotterDto.setAuthor("J.K. Rowling");
        harryPotterDto.setTitle("Harry Potter");
        harryPotterDto.setIsbn("12");
        harryPotterDto.setPrice(BigDecimal.valueOf(12.99));

        Book gameOfThrones = new Book();
        gameOfThrones.setId(2L);
        gameOfThrones.setAuthor("George Martin");
        gameOfThrones.setTitle("Game of Thrones");
        gameOfThrones.setIsbn("1234");
        gameOfThrones.setPrice(BigDecimal.valueOf(112.99));

        BookDtoWithoutCategoryIds gameOfThronesDto = new BookDtoWithoutCategoryIds();
        gameOfThronesDto.setAuthor("George Martin");
        gameOfThronesDto.setTitle("Game of Thrones");
        gameOfThronesDto.setIsbn("1234");
        gameOfThronesDto.setPrice(BigDecimal.valueOf(112.99));

        List<Book> books = List.of(harryPotter, gameOfThrones);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAllByCategoryId(2L, pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(books.get(0))).thenReturn(harryPotterDto);
        when(bookMapper.toDtoWithoutCategories(books.get(1))).thenReturn(gameOfThronesDto);

        List<BookDtoWithoutCategoryIds> booksWithoutCategoryIdExpected = List
                .of(harryPotterDto, gameOfThronesDto);
        List<BookDtoWithoutCategoryIds> booksWithoutCategoryIdActual = bookService
                .findBooksByCategoryId(2L, pageable);

        assertThat(books).hasSize(2);
        assertNotNull(books);
        assertThat(booksWithoutCategoryIdActual).isEqualTo(booksWithoutCategoryIdExpected);
    }
}
