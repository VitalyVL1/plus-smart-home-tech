package ru.practicum.circuit_braker;

import org.springframework.stereotype.Component;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.exception.ServiceTemporaryUnavailableException;
import ru.practicum.feign.client.ShoppingCartClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ShoppingCartClientFallback implements ShoppingCartClient {
    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        throw new ServiceTemporaryUnavailableException("Shopping cart service is not available");
    }

    @Override
    public ShoppingCartDto addItemToShoppingCart(String username, Map<UUID, Long> items) {
        throw new ServiceTemporaryUnavailableException("Shopping cart service is not available");
    }

    @Override
    public void deactivateShoppingCart(String username) {
        throw new ServiceTemporaryUnavailableException("Shopping cart service is not available");
    }

    @Override
    public ShoppingCartDto removeItemFromShoppingCart(String username, List<UUID> items) {
        throw new ServiceTemporaryUnavailableException("Shopping cart service is not available");
    }

    @Override
    public ShoppingCartDto changeItemQuantity(String username, ChangeProductQuantityRequest request) {
        throw new ServiceTemporaryUnavailableException("Shopping cart service is not available");
    }
}
