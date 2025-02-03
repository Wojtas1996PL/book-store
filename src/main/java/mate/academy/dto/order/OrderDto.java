package mate.academy.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Status;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Status status;
    private BigDecimal total;
    private LocalDateTime localDateTime;
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
