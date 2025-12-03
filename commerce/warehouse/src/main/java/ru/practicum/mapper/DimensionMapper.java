package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.warehouse.DimensionDto;
import ru.practicum.model.Dimension;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DimensionMapper {
    DimensionDto toDto(Dimension dimension);

    @Mapping(target = "dimensionId", ignore = true)
    Dimension toEntity(DimensionDto dimensionDto);
}
