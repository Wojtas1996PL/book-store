package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Order;
import mate.academy.model.Status;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeAnOrder(Long userId, Order order);

    List<OrderDto> getAllOrders(Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, Status status);

    List<OrderItemDto> getAllOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
