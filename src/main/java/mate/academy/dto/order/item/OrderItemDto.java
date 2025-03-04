package mate.academy.dto.order.item;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
}
