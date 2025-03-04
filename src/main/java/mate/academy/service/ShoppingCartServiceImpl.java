package mate.academy.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.cart.item.CartItemRepository;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.service.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final BookMapper bookMapper;

    @Transactional
    @Override
    public ShoppingCartDto getById(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no shopping cart with id: " + id)));
    }

    @Transactional
    @Override
    public ShoppingCartDto addBook(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = createNewShoppingCart(userId);
        if (bookRepository.findBookById(cartItemRequestDto.getBookId()).isEmpty()) {
            throw new EntityNotFoundException("Book with id: "
                    + cartItemRequestDto.getBookId() + " does not exist");
        }
        Book book = bookRepository.findBookById(cartItemRequestDto.getBookId()).get();
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateBookQuantity(Long shoppingCartId, Long cartItemId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("The shopping cart with id: "
                        + shoppingCartId + " does not exist in a database"));
        Set<CartItem> items = shoppingCart.getCartItems();
        for (CartItem item : items) {
            if (item.getId().equals(cartItemId)) {
                item.setQuantity(quantity);
                shoppingCart.setCartItems(items);
            }
        }
        return shoppingCartMapper
                .toDto(shoppingCartRepository
                        .save(shoppingCart));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private ShoppingCart createNewShoppingCart(Long userId) {
        return shoppingCartRepository.findShoppingCartById(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User with id: "
                                    + userId + " does not exist in a database")));
                    return shoppingCartRepository.save(newCart);
                });
    }
}
