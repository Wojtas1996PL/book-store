package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "toCartItemDtoList")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}
