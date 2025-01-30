package mate.academy.service.repository.shopping.cart;

import mate.academy.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {
    @Query(value = "SELECT id, user "
            + "FROM shopping_carts WHERE id=:id",
            nativeQuery = true)
    ShoppingCart findShoppingCartById(Long id);
}
