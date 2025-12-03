package ru.practicum.dto.product;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


/**
 * Запрос на установку состояния количества товара.
 *
 * @param productId     идентификатор товара
 * @param quantityState состояние количества товара
 */
public record SetProductQuantityStateRequest(
        @NotNull
        UUID productId,
        @NotNull
        QuantityState quantityState
) {
}
