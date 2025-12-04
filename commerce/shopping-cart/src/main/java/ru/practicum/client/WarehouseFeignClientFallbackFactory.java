package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

@Component
@Slf4j
public class WarehouseFeignClientFallbackFactory implements FallbackFactory<WarehouseFeignClient> {
    @Override
    public WarehouseFeignClient create(Throwable cause) {
        return new WarehouseFeignClient() {
            @Override
            public void addNewItemToWarehouse(NewProductInWarehouseRequest request) {
                log.error("Failed to add new item to warehouse", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
                log.error("Failed to check quantity in warehouse", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public void addItemToWarehouse(AddProductToWarehouseRequest request) {
                log.error("Failed to add item to warehouse", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public AddressDto getWarehouseAddress() {
                log.error("Failed to get warehouse address", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }
        };
    }
}
