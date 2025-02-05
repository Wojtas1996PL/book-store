package mate.academy.service.repository.order;

import java.util.Optional;
import mate.academy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
    @Query(value = "SELECT o "
            + "FROM Order o LEFT JOIN FETCH "
            + "o.orderItems "
            + "WHERE o.id=:id")
    Optional<Order> findOrderById(Long id);
}
