package ru.practicum.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.model.Product;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) // Для интеграции со Spring
public interface ProductMapper {

    // Entity → DTO
    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);

    default Page<ProductDto> toDto(Page<Product> page) {
        return page.map(this::toDto);
    }

    // DTO → Entity (для создания)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDto productDto);

    // Обновление Entity из DTO (для PATCH/PUT)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
}
