package mate.academy.dto.shopping.cart;

import java.util.Set;
import lombok.Data;
import mate.academy.dto.cart.item.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Long orderId;
    private Set<CartItemDto> cartItems;
}
