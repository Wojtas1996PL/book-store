package mate.academy.dto.cart.item;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long shoppingCartId;
    private Long bookId;
    private int quantity;
}
