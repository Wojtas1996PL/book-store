package mate.academy.service.repository.shopping.cart;

import java.util.Optional;
import mate.academy.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {
    @Query("SELECT DISTINCT s FROM ShoppingCart s LEFT JOIN FETCH s.cartItems WHERE s.id = :id")
    Optional<ShoppingCart> findShoppingCartById(@Param("id") Long id);
}
