package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart using id")
    @GetMapping("/api/cart")
    public ShoppingCartDto getShoppingCartById(Long id) {
        return shoppingCartService.getById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add a book to the cart")
    @PostMapping("/api/cart")
    public ShoppingCartDto addBookToCart(@RequestParam Long userId,
                                         @RequestParam Long bookId,
                                         @RequestParam int quantity) {
        return shoppingCartService.addBook(userId, bookId, quantity);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update book quantity in the cart")
    @PutMapping("/api/cart/cart-items/{cartItemId}")
    public ShoppingCartDto updateBookQuantityInTheCart(@RequestParam Long shoppingCartId,
                                                       @RequestParam Long cartItemId,
                                                       @RequestParam int quantity) {
        return shoppingCartService.updateBookQuantity(shoppingCartId, cartItemId, quantity);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete cart using id")
    @DeleteMapping("/api/cart/cart-items/{cartItemId}")
    public ShoppingCartDto deleteCartById(Long id) {
        return shoppingCartService.getById(id);
    }
}
