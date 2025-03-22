package mate.academy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.cart.item.CartItemDto;
import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    @Autowired
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext webApplicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/add-three-random-shopping-carts.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-all-shopping-carts.sql"));
        }
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Test
    @DisplayName("Verify that method getShoppingCartById works")
    public void getById_ShoppingCart_ReturnsShoppingCart() throws Exception {
        User johnDoe = new User();
        johnDoe.setId(1L);
        johnDoe.setEmail("user1@example.com");
        johnDoe.setPassword("password1");
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");

        ShoppingCart shoppingCartExpected = new ShoppingCart();
        shoppingCartExpected.setId(1L);
        shoppingCartExpected.setUser(johnDoe);
        shoppingCartExpected.setCartItems(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/api/cart/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCart shoppingCartActual = objectMapper
                .readValue(result.getResponse()
                                .getContentAsString(),
                        ShoppingCart.class);

        assertNotNull(shoppingCartActual);
        assertThat(shoppingCartActual).isEqualTo(shoppingCartExpected);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Sql(scripts = "classpath:database/add-book-alan-wake.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Verify that method addBookToCart works")
    public void add_Book_ReturnsShoppingCartDto() throws Exception {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(3L);
        cartItemDto.setShoppingCartId(1L);
        cartItemDto.setBookId(10L);
        cartItemDto.setQuantity(0);

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto();
        shoppingCartDtoExpected.setId(1L);
        shoppingCartDtoExpected.setUserId(1L);
        shoppingCartDtoExpected.setCartItems(cartItemDtos);

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setShoppingCartId(1L);
        cartItemRequestDto.setBookId(10L);

        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(post("/api/cart")
                        .param("userId", "1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartDto shoppingCartDtoActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(),
                        ShoppingCartDto.class);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected);
    }

    @WithMockUser(username = "bob", roles = "USER")
    @Sql(scripts = "classpath:database/add-book-alan-wake.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-random-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Verify that method updateBookQuantityInTheCart works")
    public void update_BookQuantity_ReturnsShoppingCartDto() throws Exception {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(5L);
        cartItemDto.setShoppingCartId(1L);
        cartItemDto.setBookId(10L);
        cartItemDto.setQuantity(200);

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto();
        shoppingCartDtoExpected.setId(1L);
        shoppingCartDtoExpected.setUserId(1L);
        shoppingCartDtoExpected.setCartItems(cartItemDtos);

        MvcResult result = mockMvc.perform(put("/api/cart/cart-items/{cartItemId}", 5)
                        .param("shoppingCartId", "1")
                        .param("quantity", "200")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto shoppingCartDtoActual = objectMapper
                .readValue(result
                        .getResponse()
                        .getContentAsString(),
                        ShoppingCartDto.class);

        assertNotNull(shoppingCartDtoActual);
        assertThat(shoppingCartDtoActual).isEqualTo(shoppingCartDtoExpected);
    }
}
