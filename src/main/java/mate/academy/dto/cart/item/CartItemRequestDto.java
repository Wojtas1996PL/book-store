package mate.academy.dto.cart.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotBlank
    private Long bookId;
    @NotBlank
    private int quantity;
}
