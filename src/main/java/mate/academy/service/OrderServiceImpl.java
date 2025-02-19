package mate.academy.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Status;
import mate.academy.model.User;
import mate.academy.service.repository.order.OrderRepository;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.service.repository.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id: "
                        + orderRequestDto.getUserId() + " does not exist in a database"));
        Order order = new Order();
        order.setUser(user);
        order.setStatus(orderRequestDto.getStatus());
        order.setShoppingCart(user.getShoppingCart());
        order.setOrderDate(orderRequestDto.getLocalDateTime());
        order.setTotal(orderRequestDto.getTotal());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        if (orderRequestDto.getOrderItems() != null) {
            order.setOrderItems(orderRequestDto.getOrderItems()
                    .stream()
                    .map(orderItemMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        if (user.getShoppingCart() == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setOrder(order);
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
            user.setShoppingCart(shoppingCart);
        } else {
            user.getShoppingCart().setOrder(order);
        }
        orderRepository.save(order);
        userRepository.save(user);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).stream().map(orderMapper::toDto).toList();
    }

    @Transactional
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
