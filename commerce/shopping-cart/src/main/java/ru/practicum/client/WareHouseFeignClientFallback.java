package ru.practicum.client;

import org.springframework.stereotype.Component;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;

@Component
public class WareHouseFeignClientFallback implements WareHouseFeignClient {
    @Override
    public void addNewItemToWarehouse(NewProductInWarehouseRequest request) {
        //Выбрасываем исключение, о том что сервис не доступен
    }

    @Override
    public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
        //Выбрасываем исключение, о том что сервис не доступен
        return null;
    }

    @Override
    public void addItemToWarehouse(AddProductToWarehouseRequest request) {
        //Выбрасываем исключение, о том что сервис не доступен
    }

    @Override
    public AddressDto getWarehouseAddress() {
        //Выбрасываем исключение, о том что сервис не доступен
        return null;
    }
}
