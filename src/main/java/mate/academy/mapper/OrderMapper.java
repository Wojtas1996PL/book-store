package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);

    Order toEntity(OrderRequestDto orderRequestDto);
}
