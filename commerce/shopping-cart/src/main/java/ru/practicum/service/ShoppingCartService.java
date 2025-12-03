package ru.practicum.service;

import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUsername(String username);

    ShoppingCartDto addItemToShoppingCart(String username, Map<UUID, Long> products, boolean mergeQuantities);

    void deactivateShoppingCartByUsername(String username);

    ShoppingCartDto removeItemFromShoppingCart(String username, List<UUID> products);

    ShoppingCartDto changeItemQuantity(String username, ChangeProductQuantityRequest request);
}
