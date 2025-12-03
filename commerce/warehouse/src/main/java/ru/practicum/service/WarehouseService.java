package ru.practicum.service;

import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewItem(NewProductInWarehouseRequest request);

    BookedProductsDto bookProducts(ShoppingCartDto shoppingCart);

    void addItem(AddProductToWarehouseRequest request);

    AddressDto getAddress();

}
