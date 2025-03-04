package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Status;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(OrderRequestDto orderRequestDto);

    List<OrderDto> getAllOrders(Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, Status status);

    List<OrderItemDto> getAllOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
