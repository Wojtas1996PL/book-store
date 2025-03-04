package mate.academy.service.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
    @Query(value = "SELECT o "
            + "FROM Order o LEFT JOIN FETCH "
            + "o.orderItems "
            + "WHERE o.id=:id")
    Optional<Order> findOrderById(Long id);

    @Override
    @Query(value = "SELECT o "
            + "FROM Order o LEFT JOIN FETCH "
            + "o.orderItems")
    Page<Order> findAll(Pageable pageable);

    @Query(value = "SELECT i FROM OrderItem i LEFT JOIN FETCH i.book b "
            + "LEFT JOIN FETCH i.order o WHERE i.order.id=:orderId")
    List<OrderItem> findAllOrderItems(@Param("orderId") Long orderId);

    @Query(value = "SELECT i FROM OrderItem i LEFT JOIN FETCH i.book b "
            + "LEFT JOIN FETCH i.order o WHERE i.id=:itemId AND i.order.id=:orderId")
    OrderItem findOrderItem(@Param("orderId") Long orderId, @Param("itemId") Long itemId);
}
