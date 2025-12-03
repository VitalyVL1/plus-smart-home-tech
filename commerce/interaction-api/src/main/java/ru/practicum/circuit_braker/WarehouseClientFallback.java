package ru.practicum.circuit_braker;

import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.practicum.exception.ServiceTemporaryUnavailableException;
import ru.practicum.feign.client.WarehouseClient;

public class WarehouseClientFallback implements WarehouseClient {
    @Override
    public void addNewItemToWarehouse(NewProductInWarehouseRequest request) {
        throw new ServiceTemporaryUnavailableException("Warehouse service is not available");
    }

    @Override
    public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
        throw new ServiceTemporaryUnavailableException("Warehouse service is not available");
    }

    @Override
    public void addItemToWarehouse(AddProductToWarehouseRequest request) {
        throw new ServiceTemporaryUnavailableException("Warehouse service is not available");
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw new ServiceTemporaryUnavailableException("Warehouse service is not available");
    }
}
