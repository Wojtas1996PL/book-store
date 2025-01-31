package mate.academy.service;

import mate.academy.dto.shopping.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getById(Long id);

    ShoppingCartDto addBook(Long userId, Long bookId, int quantity);

    ShoppingCartDto updateBookQuantity(Long shoppingCartId, Long cartItemId, int quantity);

    void deleteById(Long id);
}
