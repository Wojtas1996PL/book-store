package mate.academy.dto.order;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Status;

@Data
public class OrderRequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private Status status;
    @NotBlank
    private BigDecimal total;
    @NotBlank
    private LocalDateTime localDateTime;
    @NotBlank
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
