package ru.practicum.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

/**
 * Запрос на изменение количества товара в корзине.
 *
 * @param productId   идентификатор товара, количество которого нужно изменить, не должен быть пустым
 * @param newQuantity новое количество товара, не должно быть null
 */
public record ChangeProductQuantityRequest(
        @NotBlank
        @UUID
        String productId,
        @NotNull
        Long newQuantity
) {
}
