package mate.academy.dto.order.item;

import java.math.BigDecimal;
import lombok.Data;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.order.OrderDto;

@Data
public class OrderItemDto {
    private Long id;
    private OrderDto orderDto;
    private BookDto bookDto;
    private int quantity;
    private BigDecimal price;
}
