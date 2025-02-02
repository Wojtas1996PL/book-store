package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart using id")
    @GetMapping
    public ShoppingCartDto getShoppingCartById(Long id) {
        return shoppingCartService.getById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add a book to the cart")
    @PostMapping
    public ShoppingCartDto addBookToCart(@RequestParam Long userId,
                                         @RequestParam CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addBook(userId, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update book quantity in the cart")
    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto updateBookQuantityInTheCart(@RequestParam Long shoppingCartId,
                                                       @RequestParam Long cartItemId,
                                                       @RequestParam int quantity) {
        return shoppingCartService.updateBookQuantity(shoppingCartId, cartItemId, quantity);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete cart using id")
    @DeleteMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto deleteById(Long id) {
        return shoppingCartService.getById(id);
    }
}
