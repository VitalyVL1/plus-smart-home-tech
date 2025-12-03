package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.model.ShoppingCart;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    @Mapping(source = "shoppingCartId", target = "shoppingCartId",
            qualifiedByName = "uuidToString")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

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
