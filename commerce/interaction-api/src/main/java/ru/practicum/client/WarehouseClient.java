package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

/**
 * Клиент для работы со складом.
 */
public interface WarehouseClient {

    /**
     * Добавляет новый тип товара на склад.
     *
     * @param request данные о новом товаре
     */
    @PutMapping
    void addNewItemToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request);

    /**
     * Проверяет наличие товаров из корзины на складе.
     *
     * @param shoppingCart корзина для проверки
     * @return информация о забронированных товарах
     */
    @PostMapping("/check")
    BookedProductsDto checkQuantityInWarehouse(@RequestBody @Valid ShoppingCartDto shoppingCart);

    /**
     * Добавляет количество существующего товара на склад.
     *
     * @param request запрос на добавление товара
     */
    @PostMapping("/add")
    void addItemToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    /**
     * Получает адрес склада.
     *
     * @return адрес склада
     */
    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnToWarehouse(@RequestBody Map<UUID, Long> products);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(
            @RequestBody @Valid
            AssemblyProductsForOrderRequest request);

}