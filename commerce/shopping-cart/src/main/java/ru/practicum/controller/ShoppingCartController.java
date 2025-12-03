package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.service.ShoppingCartService;
import ru.practicum.validator.ValidCartItems;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        log.info("Getting shopping cart");
        try {
            return shoppingCartService.getShoppingCartByUsername(username);
        } catch (Exception e) {
            log.error("Error getting shopping cart", e);
            throw e;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addItemToShoppingCart(
            @RequestParam String username,
            @RequestBody @ValidCartItems Map<UUID, Long> items) {
        log.info("Updating shopping cart");
        try {
            return shoppingCartService.addItemToShoppingCart(username, items, true);
        } catch (Exception e) {
            log.error("Error updating shopping cart", e);
            throw e;
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam String username) {
        log.info("Deactivating shopping cart");
        try {
            shoppingCartService.deactivateShoppingCartByUsername(username);
        } catch (Exception e) {
            log.error("Error deactivating shopping cart", e);
            throw e;
        }
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeItemFromShoppingCart(@RequestParam String username, @RequestBody List<UUID> items) {
        log.info("Removing item(s) from shopping cart");
        try {
            return shoppingCartService.removeItemFromShoppingCart(username, items);
        } catch (Exception e) {
            log.error("Error removing item(s) from shopping cart", e);
            throw e;
        }
    }

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeItemQuantity(
            @RequestParam String username,
            @RequestBody @Valid ChangeProductQuantityRequest request) {
        log.info("Changing item quantity in shopping cart for user {}, request {}", username, request);
        try {
            return shoppingCartService.changeItemQuantity(username, request);
        } catch (Exception e) {
            log.error("Error changing item quantity in shopping cart", e);
            throw e;
        }
    }

}
