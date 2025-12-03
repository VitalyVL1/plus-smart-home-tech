package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;

//TODO
@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {
}
