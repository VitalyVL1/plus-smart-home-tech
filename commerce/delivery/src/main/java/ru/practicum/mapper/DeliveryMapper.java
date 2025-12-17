package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.model.Delivery;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AddressMapper.class})
public interface DeliveryMapper {
    DeliveryDto toDto(Delivery delivery);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Delivery toEntity(DeliveryDto deliveryDto);
}
