package mate.academy.dto.cart.item;

import lombok.Data;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;

@Data
public class CartItemDto {
    private Long id;
    private ShoppingCartDto shoppingCartDto;
    private BookDto bookDto;
    private int quantity;
}
