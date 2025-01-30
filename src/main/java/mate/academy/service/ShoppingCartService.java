package mate.academy.service;

import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.model.Book;

public interface ShoppingCartService {
    ShoppingCartDto getById(Long id);

    ShoppingCartDto addBook(ShoppingCartDto shoppingCartDto, Book book);

    ShoppingCartDto updateBookQuantity(ShoppingCartDto shoppingCartDto, Book book, int quantity);

    void deleteById(Long id);
}
