package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderDate", target = "localDateTime")
    OrderDto toDto(Order order);

    OrderDto toDto(OrderRequestDto orderRequestDto);

    Order toEntity(OrderRequestDto orderRequestDto);
}
