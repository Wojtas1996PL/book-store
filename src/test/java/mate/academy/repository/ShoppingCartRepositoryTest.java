package mate.academy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class ShoppingCartRepositoryTest {
    private static User bob;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    public static void createUser() {
        bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@gmail.com");
        bob.setPassword("password");
        bob.setFirstName("Bob");
        bob.setShippingAddress("Address");
    }

    @Test
    @DisplayName("Verify that method findShoppingCartById works")
    public void getById_ShoppingCart_ReturnsOptionalOfShoppingCart() {
        ShoppingCart shoppingCartExpected = new ShoppingCart();
        shoppingCartExpected.setId(1L);
        shoppingCartExpected.setUser(bob);
        shoppingCartExpected.setCartItems(new HashSet<>());

        shoppingCartRepository.save(shoppingCartExpected);

        ShoppingCart shoppingCartActual = shoppingCartRepository.findShoppingCartById(1L).get();

        assertNotNull(shoppingCartActual);
        assertThat(shoppingCartActual).isEqualTo(shoppingCartExpected);
    }
}
