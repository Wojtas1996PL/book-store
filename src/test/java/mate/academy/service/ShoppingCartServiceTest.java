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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify that method getById works")
    public void getById_ShoppingCartWithCorrectId_ReturnsShoppingCartDto() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Marley");
        user.setEmail("bob@gmail.com");
        user.setPassword("password");
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33));
        order.setShippingAddress("Poland");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setOrder(order);
        shoppingCart.setDeleted(false);

        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto();
        shoppingCartDtoExpected.setId(1L);
        shoppingCartDtoExpected.setUserId(1L);
        shoppingCartDtoExpected.setOrderId(1L);

        when(shoppingCartRepository.findShoppingCartById(1L)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDtoExpected);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService.getById(1L);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Verify that method getById throws exception when id is incorrect")
    public void getById_ShoppingCartWithIncorrectId_ReturnsShoppingCartDto() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.getById(100L));
    }

    @Test
    @DisplayName("Verify that method addBook works")
    public void add_Book_ReturnsShoppingCartDto() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book");
        book.setAuthor("Author");
        book.setIsbn("500");
        book.setPrice(BigDecimal.valueOf(100));
        book.setDeleted(false);

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setShoppingCartId(1L);
        cartItemRequestDto.setBookId(1L);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setShoppingCartId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setQuantity(200);

        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Marley");
        user.setEmail("bob@gmail.com");
        user.setPassword("password");
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33));
        order.setShippingAddress("Poland");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setOrder(order);
        shoppingCart.setDeleted(false);

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setId(1L);
        cartItem.setQuantity(200);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        shoppingCart.setCartItems(cartItems);

        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto();
        shoppingCartDtoExpected.setId(1L);
        shoppingCartDtoExpected.setUserId(1L);
        shoppingCartDtoExpected.setOrderId(1L);
        shoppingCartDtoExpected.setCartItems(cartItemDtos);

        when(shoppingCartRepository.findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findBookById(cartItemRequestDto.getBookId()))
                .thenReturn(Optional.of(book));
        when(shoppingCartRepository.save(shoppingCart))
                .thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDtoExpected);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService.addBook(1L, cartItemRequestDto);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(bookRepository, times(2)).findBookById(1L);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Verify that method addBook throws Exception when book does not exist")
    public void add_IncorrectBook_ThrowsEntityNotFoundException() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setShoppingCartId(1L);
        cartItemRequestDto.setBookId(100L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setDeleted(false);

        when(shoppingCartRepository
                .findShoppingCartById(1L))
                .thenReturn(Optional.of(shoppingCart));

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.addBook(1L, cartItemRequestDto));
    }

    @Test
    @DisplayName("Verify that method updateBookQuantity works")
    public void update_BookQuantity_ReturnsShoppingCartDto() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Marley");
        user.setEmail("bob@gmail.com");
        user.setPassword("password");
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33));
        order.setShippingAddress("Poland");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setOrder(order);
        shoppingCart.setDeleted(false);
        shoppingCart.setCartItems(new HashSet<>());

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setQuantity(500);
        cartItemDto.setBookId(1L);

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto();
        shoppingCartDtoExpected.setId(1L);
        shoppingCartDtoExpected.setUserId(1L);
        shoppingCartDtoExpected.setOrderId(1L);
        shoppingCartDtoExpected.setCartItems(cartItemDtos);

        when(shoppingCartRepository.findShoppingCartById(1L)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDtoExpected);

        ShoppingCartDto shoppingCartDtoActual = shoppingCartService
                .updateBookQuantity(1L, 1L, 500);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected);

        verify(shoppingCartRepository, times(1)).findShoppingCartById(1L);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Verify that method updateBookQuantity throws "
            + "EntityNotFoundException when shopping cart does not exist")
    public void update_BookQuantityWithIncorrectShoppingCartId_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService
                .updateBookQuantity(100L, 1L, 20));
    }
}
