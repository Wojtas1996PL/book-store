package mate.academy.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.service.repository.book.BookRepository;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.service.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getById(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no shopping cart with id: " + id)));
    }

    @Override
    public ShoppingCartDto addBook(Long userId, Long bookId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User with id: "
                                    + userId + " does not exist in a database")));
                    return shoppingCartRepository.save(newCart);
                });
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id: " + bookId + " not found"));
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
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
        shoppingCartRepository.deleteById(id);
    }
}
