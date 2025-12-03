package ru.practicum.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.util.Map;

/**
 * DTO корзины покупок.
 * Содержит идентификатор корзины и список идентификаторов товаров.
 *
 * @param shoppingCartId идентификатор корзины, не должен быть пустым
 * @param products       список идентификаторов товаров в корзине, не должен быть null
 */
public record ShoppingCartDto(
        @NotBlank
        @UUID
        String shoppingCartId,
        @NotNull
        Map<java.util.UUID, Long> products
) {
}
