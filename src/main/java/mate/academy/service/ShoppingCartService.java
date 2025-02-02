package mate.academy.service;

import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getById(Long id);

    ShoppingCartDto addBook(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateBookQuantity(Long shoppingCartId, Long cartItemId, int quantity);

    void deleteById(Long id);
}
