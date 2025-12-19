package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;
import ru.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления складом.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addNewItemToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.addNewItem(request);
    }

    @Override
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantityInWarehouse(@RequestBody @Valid ShoppingCartDto shoppingCart) {
        return warehouseService.checkQuantityInWarehouse(shoppingCart);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addItemToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addItem(request);
    }

    @Override
    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWarehouseAddress() {
        return warehouseService.getAddress();
    }

    @Override
    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    public void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnToWarehouse(@RequestBody Map<UUID, Long> products) {
        warehouseService.returnToWarehouse(products);
    }

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(
            @RequestBody @Valid
            AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProductForOrder(request);
    }

    @Override
    @PostMapping("/assembly/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelAssemblyProductForOrder(UUID orderId) {
        warehouseService.cancelAssemblyProductForOrder(orderId);
    }
}