package ru.practicum.service;

import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис для управления складом.
 */
public interface WarehouseService {

    /**
     * Добавляет новый тип товара на склад.
     *
     * @param request данные о новом товаре
     */
    void addNewItem(NewProductInWarehouseRequest request);

    /**
     * Проверяет наличие товаров из корзины на складе.
     *
     * @param shoppingCart корзина для проверки
     * @return информация о забронированных товарах
     */
    BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart);

    /**
     * Добавляет количество существующего товара на склад.
     *
     * @param request запрос на добавление товара
     */
    void addItem(AddProductToWarehouseRequest request);

    /**
     * Получает адрес склада.
     *
     * @return адрес склада
     */
    AddressDto getAddress();

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void returnToWarehouse(Map<UUID, Long> products);

    BookedProductsDto assemblyProductForOrder(AssemblyProductsForOrderRequest request);

    void cancelAssemblyProductForOrder(UUID orderId);
}