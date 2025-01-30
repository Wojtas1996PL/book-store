package mate.academy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "shopping_carts")
@Data
public class ShoppingCart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private User user;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "shopping_cart_id"),
            inverseJoinColumns = @JoinColumn(name = "cart_item_id")
    )
    private Set<CartItem> cartItems;
}
