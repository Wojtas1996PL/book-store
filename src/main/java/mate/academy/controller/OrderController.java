package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.Order;
import mate.academy.model.Status;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order")
    @PostMapping
    public OrderDto placeAnOrder(Order order) {
        return orderService.placeAnOrder(order);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get orders list from user")
    @GetMapping
    public List<OrderDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@RequestParam Long orderId, Status status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all order items from order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItems(@RequestParam Long orderId) {
        return orderService.getAllOrderItems(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get specific order items from order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(@RequestParam Long orderId,
                                     @RequestParam Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
