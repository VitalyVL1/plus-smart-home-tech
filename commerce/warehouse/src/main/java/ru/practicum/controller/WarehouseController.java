package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.practicum.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
@Slf4j
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addNewItemToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request) {
        log.info("Adding new item to warehouse");
        try {
            warehouseService.addNewItem(request);
        } catch (Exception e) {
            log.error("Error adding new item to warehouse", e);
            throw e;
        }
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantityInWarehouse(@RequestBody @Valid ShoppingCartDto shoppingCart) {
        log.info("Checking quantity in warehouse");
        try {
            return warehouseService.bookProducts(shoppingCart);
        } catch (Exception e) {
            log.error("Error checking quantity in warehouse", e);
            throw e;
        }
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addItemToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        log.info("Adding item to warehouse");
        try {
            warehouseService.addItem(request);
        } catch (Exception e) {
            log.error("Error adding item to warehouse", e);
            throw e;
        }
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWarehouseAddress() {
        log.info("Getting warehouse address");
        try {
            return warehouseService.getAddress();
        } catch (Exception e) {
            log.error("Error getting warehouse address", e);
            throw e;
        }
    }
}
