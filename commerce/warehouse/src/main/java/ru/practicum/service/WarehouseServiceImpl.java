package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.product.QuantityState;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.feign.client.ShoppingStoreClient;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.DimensionMapper;
import ru.practicum.model.BookedProduct;
import ru.practicum.model.Warehouse;
import ru.practicum.model.WarehouseProduct;
import ru.practicum.repository.BookedProductRepository;
import ru.practicum.repository.WarehouseProductRepository;
import ru.practicum.repository.WarehouseRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final BookedProductRepository bookedProductRepository;
    private final AddressMapper addressMapper;
    private final DimensionMapper dimensionMapper;
    private final ShoppingStoreClient shoppingStoreClient;

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

    @Override
    public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
        Map<UUID, Long> shoppingCartProducts = shoppingCart.products();

        List<WarehouseProduct> products = warehouseProductRepository
                .findAllByProductIdIn(shoppingCartProducts.keySet());

        Map<UUID, Long> availableProducts = products.stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId,
                        WarehouseProduct::getQuantity,
                        Long::sum));

        List<UUID> notEnoughProducts = shoppingCartProducts.entrySet().stream()
                .filter(entry -> {
                    Long available = availableProducts.get(entry.getKey());
                    return available == null || available < entry.getValue();
                }).map(Map.Entry::getKey)
                .toList();

        if (!notEnoughProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products in warehouse, ids = " +
                                                                  notEnoughProducts);
        }

        // рассчитываем вес и объем доставки
        Double deliveryWeight = products.stream()
                .mapToDouble(wp -> wp.getWeight() * shoppingCartProducts.get(wp.getProductId()))
                .sum();
        Double deliveryVolume = products.stream()
                .mapToDouble(wp -> wp.getDimensions().getVolume() * shoppingCartProducts.get(wp.getProductId()))
                .sum();
        Boolean fragile = products.stream().anyMatch(WarehouseProduct::getFragile);

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    @Transactional
    @Override
    public void addItem(AddProductToWarehouseRequest request) {
        UUID productId = UUID.fromString(request.productId());

        //в рамках ТЗ работаем с одним складом, то будет только 1 элемент или пусто
        // TODO: в случае работы с несколькими складами, нужно будет:
        // 1. менять логику самого запроса (указывать конкретный склад)
        // 2. менять логику процесса добавления на конкретный склад
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        String.format("Product which id = %s not found", productId)));

        warehouseProduct.setQuantity(warehouseProduct.getQuantity() + request.quantity());

        updateQuantityState(warehouseProduct);
    }

    @Transactional
    @Override
    public void bookProducts(ShoppingCartDto shoppingCart) {
        Map<UUID, Long> shoppingCartProducts = shoppingCart.products();

        List<WarehouseProduct> products = warehouseProductRepository
                .findAllByProductIdIn(shoppingCartProducts.keySet());

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
        List<BookedProduct> bookedProducts =
                products.stream()
                        .map(product -> BookedProduct.builder()
                                .shoppingCartId(UUID.fromString(shoppingCart.shoppingCartId()))
                                .warehouseProduct(product)
                                .quantity(shoppingCartProducts.get(product.getProductId()))
                                .build())
                        .toList();

        // сохраняем забронированные товары в базу данных
        bookedProductRepository.saveAll(bookedProducts);

        // уменьшаем количество товара на складе
        products.forEach(product ->
                product.setQuantity(product.getQuantity() - shoppingCartProducts.get(product.getProductId())));

        products.forEach(this::updateQuantityState); //обновляем статус в магазине
    }

    @Override
    public AddressDto getAddress() {
        // в рамках ТЗ работаем с одним складом,
        // то не важно адрес какого склада мы запрашиваем
        // TODO: при добавлении поддержки работы с несколькими складами необходимо:
        // 1. менять логику самого запроса
        // 2. менять логику получения адреса склада
        Warehouse warehouse = getRandomWarehouse();
        return addressMapper.toDto(warehouse.getAddress());
    }

    private Warehouse getRandomWarehouse() {
        // в рамках ТЗ работаем с одним рандомным складом (инициализироваться БД будет с 1 складом)
        return warehouseRepository.findAll().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No warehouses found"));
    }

    private void updateQuantityState(WarehouseProduct warehouseProduct) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(
                warehouseProduct.getProductId().toString(),
                determineQuantityState(warehouseProduct.getQuantity()));

        if (!shoppingStoreClient.setQuantityState(request)) {
            throw new RuntimeException("Unable to set quantity state");
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
}
