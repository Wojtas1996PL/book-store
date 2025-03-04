package mate.academy.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.config.MapperConfig;
import mate.academy.dto.cart.item.CartItemDto;
import mate.academy.dto.cart.item.CartItemRequestDto;
import mate.academy.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toEntity(CartItemDto cartItemDto);

    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    @Mapping(source = "shoppingCart.id", target = "shoppingCartId")
    @Mapping(source = "book.id", target = "bookId")
    CartItemDto toDto(CartItem cartItem);

    CartItemDto toDto(CartItemRequestDto cartItemRequestDto);

    @Named("toCartItemDtoList")
    default Set<CartItemDto> toCartItemDtoList(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
