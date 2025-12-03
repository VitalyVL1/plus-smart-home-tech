package ru.practicum.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.model.Product;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) // Для интеграции со Spring
public interface ProductMapper {

    // Entity → DTO
    @Mapping(source = "productId", target = "productId",
            qualifiedByName = "uuidToString")
    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);

    default Page<ProductDto> toDto(Page<Product> page) {
        return page.map(this::toDto);
    }

    // DTO → Entity (для создания)
    @Mapping(source = "productId", target = "productId",
            qualifiedByName = "stringToUuid")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDto productDto);

    // Обновление Entity из DTO (для PATCH/PUT)
    @Mapping(source = "productId", target = "productId",
            qualifiedByName = "stringToUuid")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductDto productDto, @MappingTarget Product product);

    // Кастомные методы конвертации
    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String str) {
        return str != null ? UUID.fromString(str) : null;
    }
}
