package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.item.OrderItemDto;
import mate.academy.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemDto orderItemDto);
}
