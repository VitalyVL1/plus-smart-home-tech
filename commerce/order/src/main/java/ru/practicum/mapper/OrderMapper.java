package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.model.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderDto toDto(Order order);

    default Page<OrderDto> toDto(Page<Order> page) {
        return page.map(this::toDto);
    }
}
