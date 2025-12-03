package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.model.Address;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    AddressDto toDto(Address address);

    @Mapping(target = "addressId", ignore = true)
    Address toEntity(AddressDto addressDto);
}
