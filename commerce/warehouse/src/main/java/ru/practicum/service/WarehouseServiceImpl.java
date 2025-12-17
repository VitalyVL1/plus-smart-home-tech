package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.ShoppingStoreFeignClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.product.QuantityState;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.dto.warehouse.*;
import ru.practicum.exception.*;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.DimensionMapper;
import ru.practicum.model.BookedProduct;
import ru.practicum.model.Warehouse;
import ru.practicum.model.WarehouseProduct;
import ru.practicum.repository.BookedProductRepository;
import ru.practicum.repository.WarehouseProductRepository;
import ru.practicum.repository.WarehouseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления складом.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final TransactionTemplate transactionTemplate;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final BookedProductRepository bookedProductRepository;
    private final AddressMapper addressMapper;
    private final DimensionMapper dimensionMapper;
    private final ShoppingStoreFeignClient shoppingStoreClient;

    @Transactional
    @Override
    public void addNewItem(NewProductInWarehouseRequest request) {
        Warehouse warehouse = getRandomWarehouse();
        UUID productId = UUID.fromString(request.productId());
        List<WarehouseProduct> products = warehouse.getProducts();

        if (products.stream()
                .anyMatch(p -> p.getProductId().equals(productId))) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already specified in warehouse, id = " +
                                                                  productId);
        }

        WarehouseProduct newProduct = WarehouseProduct.builder()
                .warehouse(warehouse)
                .productId(productId)
                .fragile(request.fragile())
                .dimensions(dimensionMapper.toEntity(request.dimension()))
                .weight(request.weight())
                .quantity(0L)
                .build();

        products.add(newProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
        Map<UUID, Long> shoppingCartProducts = shoppingCart.products();
        List<WarehouseProduct> availableProducts = checkAvailableProducts(shoppingCartProducts);
        return determineBookedProducts(availableProducts, shoppingCartProducts);
    }

    //в рамках ТЗ работаем с одним складом, то будет только 1 элемент или пусто
    // TODO: в случае работы с несколькими складами, нужно будет:
    // 1. менять логику самого запроса (указывать конкретный склад)
    // 2. менять логику процесса добавления на конкретный склад
    @Override
    public void addItem(AddProductToWarehouseRequest request) {

        WarehouseProduct updatedWarehouseProduct = transactionTemplate.execute(status -> {
            WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductId(request.productId())
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            String.format("Product which id = %s not found", request.productId())));

            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + request.quantity());

            return warehouseProduct;
        });

        updateQuantityState(updatedWarehouseProduct); //обновляем состояние на складе
    }

    // TODO: При добавлении поддержки нескольких складов необходимо:
    // 1. Изменить логику резервирования - распределять товары между складами
    // 2. Добавить алгоритм выбора складов (ближайший, с наибольшим запасом и т.д.)
    // 3. Учитывать остатки на каждом складе при распределении
    // 4. Возможно добавить приоритеты складов
    // Пример будущей реализации:
    // Map<UUID, Long> remainingDemand = new HashMap<>(shoppingCartProducts);
    // for (каждый товар в заказе) {
    //   for (каждый склад с этим товаром) {
    //     Long reserve = Math.min(remainingDemand.get(productId), stock.getQuantity());
    //     // создать BookedProduct с reserve количеством
    //     // уменьшить remainingDemand и остатки на складе
    //   }
    // }
    @Override
    @Transactional
    public BookedProductsDto assemblyProductForOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> assemblyProducts = request.products();

        List<WarehouseProduct> products = checkAvailableProducts(assemblyProducts);

        // Создаем бронирования
        List<BookedProduct> bookedProducts = products.stream()
                .map(product -> BookedProduct.builder()
                        .orderId(request.orderId())
                        .warehouseProduct(product)
                        .quantity(assemblyProducts.get(product.getProductId()))
                        .build())
                .toList();

        bookedProductRepository.saveAll(bookedProducts);

        // Уменьшаем количество
        products.forEach(product ->
                product.setQuantity(
                        product.getQuantity() -
                        assemblyProducts.get(product.getProductId())));

        // updateQuantityState - не бросит исключение
        products.forEach(this::updateQuantityState);

        return determineBookedProducts(products, assemblyProducts);
    }

    // в рамках ТЗ работаем с одним складом,
    // то не важно адрес какого склада мы запрашиваем
    // TODO: при добавлении поддержки работы с несколькими складами необходимо:
    // 1. менять логику самого запроса
    // 2. менять логику получения адреса склада
    @Override
    public AddressDto getAddress() {
        Warehouse warehouse = getRandomWarehouse();
        return addressMapper.toDto(warehouse.getAddress());
    }

    // По спецификации метод не выбрасывает исключения если заказ отсутствует
    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        List<BookedProduct> bookedProducts =
                bookedProductRepository.findAllByOrderId(request.orderId());

        bookedProducts.forEach(product -> product.setDeliveryId(request.deliveryId()));
    }

    // По спецификации метод не выбрасывает исключения если товар не найден
    @Override
    @Transactional
    public void returnToWarehouse(Map<UUID, Long> products) {
        List<WarehouseProduct> productsToReturn = warehouseProductRepository.findAllByProductIdIn(products.keySet());
        productsToReturn.forEach(product ->
                product.setQuantity(product.getQuantity() + products.get(product.getProductId())));
        productsToReturn.forEach(this::updateQuantityState);
    }

    private Warehouse getRandomWarehouse() {
        // в рамках ТЗ работаем с одним рандомным складом (инициализироваться БД будет с 1 складом)
        return warehouseRepository.findAll().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No warehouses found"));
    }

    private void updateQuantityState(WarehouseProduct warehouseProduct) {
        if (warehouseProduct == null) {
            return;
        }

        QuantityState quantityState = determineQuantityState(warehouseProduct.getQuantity());

        log.debug("Setting quantity state for product {} to {}", warehouseProduct.getProductId(), quantityState);

        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(
                warehouseProduct.getProductId(),
                determineQuantityState(warehouseProduct.getQuantity()));
        //отправляем данные в магазин, в случае любой ошибки просто логируем и продложаем работу
        log.debug("Sending request to set quantity state for product {} to {}",
                warehouseProduct.getProductId(), quantityState);
        try {
            shoppingStoreClient.setQuantityState(request);
        } catch (ResourceNotFoundException e) {
            log.warn("Product {} not in store", warehouseProduct.getProductId());
        } catch (BadRequestException e) {
            log.warn("Invalid quantity state for product {}", warehouseProduct.getProductId());
        } catch (ServiceTemporaryUnavailableException e) {
            log.warn("Service temporary unavailable {}", warehouseProduct.getProductId());
        } catch (Exception e) {
            log.warn("Unknown error {}", warehouseProduct.getProductId());
        }
    }

    private QuantityState determineQuantityState(Long quantity) {
        if (quantity == 0) {
            return QuantityState.ENDED;
        } else if (quantity < 10) {
            return QuantityState.FEW;
        } else if (quantity < 100) {
            return QuantityState.ENOUGH;
        } else {
            return QuantityState.MANY;
        }
    }

    private List<WarehouseProduct> checkAvailableProducts(Map<UUID, Long> checkingProducts) {

        if (checkingProducts == null || checkingProducts.isEmpty()) {
            return Collections.emptyList();
        }

        List<WarehouseProduct> products = warehouseProductRepository
                .findAllByProductIdIn(checkingProducts.keySet());

        Map<UUID, Long> availableProducts = products.stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId,
                        WarehouseProduct::getQuantity,
                        Long::sum));

        List<UUID> notEnoughProducts = checkingProducts.entrySet().stream()
                .filter(entry -> {
                    Long available = availableProducts.get(entry.getKey());
                    return available == null || available < entry.getValue();
                }).map(Map.Entry::getKey)
                .toList();

        if (!notEnoughProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Out of stock products ids: { " +
                                                                  notEnoughProducts + " }");
        }

        return products;
    }

    private BookedProductsDto determineBookedProducts(
            List<WarehouseProduct> availableProducts,
            Map<UUID, Long> assemblyProducts) {

        if (availableProducts == null || availableProducts.isEmpty()
            || assemblyProducts == null || assemblyProducts.isEmpty()) {
            return BookedProductsDto.builder().build();
        }

        Double deliveryWeight = availableProducts.stream()
                .mapToDouble(wp ->
                        wp.getWeight() * assemblyProducts.get(wp.getProductId()))
                .sum();

        Double deliveryVolume = availableProducts.stream()
                .mapToDouble(wp ->
                        wp.getDimensions().getVolume() * assemblyProducts.get(wp.getProductId()))
                .sum();

        Boolean fragile = availableProducts.stream().anyMatch(WarehouseProduct::getFragile);

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }
}
