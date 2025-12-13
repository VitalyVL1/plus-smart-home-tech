package ru.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddressDto;

public record CreateNewOrderRequest(
        @NotNull
        ShoppingCartDto shoppingCart,
        @NotNull
        AddressDto deliveryAddress
) {
}
