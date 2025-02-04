package mate.academy.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.Status;
import mate.academy.model.User;
import mate.academy.service.repository.order.OrderRepository;
import mate.academy.service.repository.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderDto placeAnOrder(Long userId, Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: "
                        + userId + " does not exist in a database"));
        user.getShoppingCart().setOrder(order);
        userRepository.save(user);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("The order with id: "
                        + orderId + " does not exist in a database"));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("The order with id: "
                        + orderId + " does not exist in a database"));
        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("The order with id: "
                        + orderId + " does not exist in a database"));
        OrderItem item = new OrderItem();
        Set<OrderItem> items = order.getOrderItems();
        for (OrderItem orderItem : items) {
            if (orderItem.getId().equals(itemId)) {
                item = orderItem;
            }
        }
        return orderItemMapper.toDto(item);
    }
}
