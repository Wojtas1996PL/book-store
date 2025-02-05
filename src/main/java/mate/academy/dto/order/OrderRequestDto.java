package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Status;

public class OrderRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Status status;
    @NotNull
    private BigDecimal total;
    @NotNull
    private LocalDateTime localDateTime;
    @NotNull
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
