package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.annotation.validator.ValidCartItems;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addItemToShoppingCart(
            @RequestParam String username,
            @RequestBody @ValidCartItems Map<UUID, Long> items);

    @DeleteMapping
    void deactivateShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeItemFromShoppingCart(@RequestParam String username, @RequestBody List<UUID> items);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeItemQuantity(
            @RequestParam String username,
            @RequestBody @Valid ChangeProductQuantityRequest request);
}