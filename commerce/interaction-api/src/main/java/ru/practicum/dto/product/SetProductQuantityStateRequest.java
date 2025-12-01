package ru.practicum.dto.product;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;


/**
 * Запрос на установку состояния количества товара.
 *
 * @param productId     идентификатор товара
 * @param quantityState состояние количества товара
 */
public record SetProductQuantityStateRequest(
        @UUID
        @NotNull
        String productId,
        @NotNull
        QuantityState quantityState
) {
}
