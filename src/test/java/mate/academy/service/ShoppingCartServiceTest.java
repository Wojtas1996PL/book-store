package mate.academy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.dto.cart.item.CartItemDto;
import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Status;
import mate.academy.model.User;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static Book book;

    private static User user;

    private static Order order;

    private static ShoppingCartDto shoppingCartDtoExpected1;

    private static ShoppingCartDto shoppingCartDtoExpected2;

    private static ShoppingCart shoppingCart1;

    private static ShoppingCart shoppingCart2;

    private static CartItemRequestDto cartItemRequestDto;

    private static CartItemRequestDto cartItemRequestDtoIncorrectBook;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private BookRepository bookRepository;

    @BeforeAll
    public static void createObjects() {
        user = new User();
        user.setFirstName("Bob");
        user.setLastName("Marley");
        user.setEmail("bob@gmail.com");
        user.setPassword("password");
        user.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33));
        order.setShippingAddress("Poland");

        book = new Book();
        book.setId(1L);
        book.setTitle("Book");
        book.setAuthor("Author");
        book.setIsbn("500");
        book.setPrice(BigDecimal.valueOf(100));
        book.setDeleted(false);

        shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);
        shoppingCart1.setUser(user);
        shoppingCart1.setOrder(order);
        shoppingCart1.setDeleted(false);
        shoppingCart1.setCartItems(new HashSet<>());

        cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setShoppingCartId(1L);
        cartItemRequestDto.setBookId(1L);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setShoppingCartId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setQuantity(200);

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart1);
        cartItem.setBook(book);
        cartItem.setId(1L);
        cartItem.setQuantity(200);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        shoppingCart1.setCartItems(cartItems);

        shoppingCartDtoExpected1 = new ShoppingCartDto();
        shoppingCartDtoExpected1.setId(1L);
        shoppingCartDtoExpected1.setUserId(1L);
        shoppingCartDtoExpected1.setOrderId(1L);
        shoppingCartDtoExpected1.setCartItems(cartItemDtos);

        cartItemRequestDtoIncorrectBook = new CartItemRequestDto();
        cartItemRequestDtoIncorrectBook.setShoppingCartId(1L);
        cartItemRequestDtoIncorrectBook.setBookId(100L);

        shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(1L);
        shoppingCart2.setUser(user);
        shoppingCart2.setOrder(order);
        shoppingCart2.setDeleted(false);
        shoppingCart2.setCartItems(new HashSet<>());

        CartItemDto cartItemDto2 = new CartItemDto();
        cartItemDto2.setId(1L);
        cartItemDto2.setQuantity(500);
        cartItemDto2.setBookId(1L);

        Set<CartItemDto> cartItemDtos2 = new HashSet<>();
        cartItemDtos2.add(cartItemDto2);

        shoppingCartDtoExpected2 = new ShoppingCartDto();
        shoppingCartDtoExpected2.setId(1L);
        shoppingCartDtoExpected2.setUserId(1L);
        shoppingCartDtoExpected2.setOrderId(1L);
        shoppingCartDtoExpected2.setCartItems(cartItemDtos2);
    }

    @Test
    @DisplayName("Verify that method getById works")
    public void getById_ShoppingCartWithCorrectId_ReturnsShoppingCartDto() {
        when(shoppingCartRepository.findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart1));
        when(shoppingCartMapper.toDto(shoppingCart1)).thenReturn(shoppingCartDtoExpected1);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService.getById(1L);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected1);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart1);
    }

    @Test
    @DisplayName("Verify that method getById throws EntityNotFoundException "
            + "when shopping cart does not exist")
    public void getById_ShoppingCartWithIncorrectId_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.getById(100L));
    }

    @Test
    @DisplayName("Verify that method getById throws exception when id is incorrect")
    public void getById_ShoppingCartWithIncorrectId_ReturnsShoppingCartDto() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.getById(100L));
    }

    @Test
    @DisplayName("Verify that method addBook works")
    public void add_Book_ReturnsShoppingCartDto() {
        when(shoppingCartRepository.findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart1));
        when(bookRepository.findBookById(cartItemRequestDto.getBookId()))
                .thenReturn(Optional.of(book));
        when(shoppingCartRepository.save(shoppingCart1))
                .thenReturn(shoppingCart1);
        when(shoppingCartMapper.toDto(shoppingCart1))
                .thenReturn(shoppingCartDtoExpected1);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService.addBook(1L, cartItemRequestDto);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected1);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(bookRepository, times(2)).findBookById(1L);
        verify(shoppingCartRepository, times(1)).save(shoppingCart1);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart1);
    }

    @Test
    @DisplayName("Verify that method addBook throws Exception when book does not exist")
    public void add_IncorrectBook_ThrowsEntityNotFoundException() {
        when(shoppingCartRepository
                .findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart1));

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.addBook(1L, cartItemRequestDtoIncorrectBook));
    }

    @Test
    @DisplayName("Verify that method updateBookQuantity works")
    public void update_BookQuantity_ReturnsShoppingCartDto() {
        when(shoppingCartRepository.findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart2));
        when(shoppingCartRepository.save(shoppingCart2)).thenReturn(shoppingCart2);
        when(shoppingCartMapper.toDto(shoppingCart2)).thenReturn(shoppingCartDtoExpected2);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService
                .updateBookQuantity(1L, 1L, 500);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected2);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(shoppingCartRepository, times(1)).save(shoppingCart2);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart2);
    }

    @Test
    @DisplayName("Verify that method updateBookQuantity throws "
            + "EntityNotFoundException when shopping cart does not exist")
    public void update_BookQuantityWithIncorrectShoppingCartId_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService
                .updateBookQuantity(100L, 1L, 20));
    }
}
