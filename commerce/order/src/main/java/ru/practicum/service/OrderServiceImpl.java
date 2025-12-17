package ru.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.DeliveryClient;
import ru.practicum.client.PaymentClient;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.OrderState;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.exception.NoOrderFoundException;
import ru.practicum.exception.NotAuthorizedUserException;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.OrderMapper;
import ru.practicum.model.Order;
import ru.practicum.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Page<OrderDto> getOrders(String username, Pageable pageable) {
        if (username == null) {
            throw new NotAuthorizedUserException("User =  " + username + " is not authorized");
        }

        return orderMapper.toDto(orderRepository.findByUsername(username, pageable));
    }

    //TODO: для корректной реализации патерна SAGA необходимы также методы для отката бронирования товаров
    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        // 1. Создание заказа и бронирование товаров на складе
        Order order = createOrderEntity(request);

        // 2. Планирование доставки
        planDelivery(order, request.deliveryAddress());

        // 3. Расчет стоимости
        calculateAndSetPrices(order);

        // 4. Создание платежа
        createPayment(order);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrder(request.getOrderId());

        if (order.getState() != OrderState.PRODUCT_RETURNED) {
            warehouseClient.returnToWarehouse(request.getProducts());
        }

        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(order);
    }

    // Вызывается из сервиса payment
    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        return setOrderState(orderId, OrderState.PAID);
    }

    // Вызывается из сервиса payment
    @Override
    public OrderDto paymentFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    // Вызывается из сервиса delivery
    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.DELIVERED);
    }

    // Вызывается из сервиса delivery
    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    // Вызов сторонним сервисом в рамках ТЗ не предусмотрен
    @Override
    public OrderDto completedOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.COMPLETED);
    }

    @Override
    @Transactional
    public OrderDto calculateTotal(UUID orderId) {
        Order order = getOrder(orderId);
        order.setTotalPrice(paymentClient.getTotalCost(orderMapper.toDto(order)));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateDelivery(UUID orderId) {
        Order order = getOrder(orderId);
        order.setDeliveryPrice(deliveryClient.deliveryCost(orderMapper.toDto(order)));
        return orderMapper.toDto(order);
    }

    // вызывается из delivery
    @Override
    @Transactional
    public OrderDto assemblyOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.ASSEMBLED);
    }

    // Вызов сторонним сервисом в рамках ТЗ не предусмотрен
    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private OrderDto setOrderState(UUID orderId, OrderState state) {
        if (orderId == null || state == null) {
            throw new IllegalArgumentException("Order id and state cannot be null");
        }
        return transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NoOrderFoundException("No order found for id = " + orderId));
            if (order.getState() == state) {
                log.info("Order {} is already in state {}", orderId, state);
            } else {
                log.info("Order {} is in state {}, changing to {}", orderId, order.getState(), state);
                order.setState(state);
            }
            return orderMapper.toDto(order);
        });
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id = " + orderId));
    }

    private Order createOrderEntity(CreateNewOrderRequest request) {
        ShoppingCartDto shoppingCart = request.shoppingCart();

        // 1. Создаем базовый заказ и делаем промежуточное сохранение для получения ID
        Order order = Order.builder()
                .shoppingCartId(shoppingCart.shoppingCartId())
                .products(shoppingCart.products())
                .state(OrderState.NEW)
                .username(shoppingCart.username())
                .address(addressMapper.toEntity(request.deliveryAddress()))
                .build();

        order = orderRepository.save(order);

        // 2. Создаем запрос на сборку товаров
        AssemblyProductsForOrderRequest assemblyRequest =
                new AssemblyProductsForOrderRequest(order.getProducts(), order.getOrderId());

        BookedProductsDto bookedProducts = warehouseClient.assemblyProductForOrderFromShoppingCart(assemblyRequest);

        order.setDeliveryWeight(bookedProducts.deliveryWeight());
        order.setDeliveryVolume(bookedProducts.deliveryVolume());
        order.setFragile(bookedProducts.fragile());

        return orderRepository.save(order);
    }

    private void planDelivery(Order order, AddressDto deliveryAddress) {
        AddressDto fromAddress = warehouseClient.getWarehouseAddress();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(fromAddress)
                .toAddress(deliveryAddress)
                .orderId(order.getOrderId())
                .build();

        deliveryDto = deliveryClient.planDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.deliveryId());
        orderRepository.save(order);
    }

    private void calculateAndSetPrices(Order order) {
        OrderDto orderDto = orderMapper.toDto(order);

        //Получаем стоимости доставки и товаров
        BigDecimal deliveryPrice = deliveryClient.deliveryCost(orderDto);
        BigDecimal productPrice = paymentClient.productCost(orderDto);

        //Устанавливаем стоимость доставки и товаров
        //для дальнейшего расчета общей стоимости
        order.setDeliveryPrice(deliveryPrice);
        order.setProductPrice(productPrice);

        //Получаем общую стоимость заказа
        BigDecimal totalPrice = paymentClient.getTotalCost(orderMapper.toDto(order));
        order.setTotalPrice(totalPrice);

        //Обновляем данные в репозитории
        orderRepository.save(order);
    }

    private void createPayment(Order order) {
        PaymentDto paymentDto = paymentClient.payment(orderMapper.toDto(order));
        order.setPaymentId(paymentDto.paymentId());
        orderRepository.save(order);
        log.debug("Payment {} created for order {}", paymentDto.paymentId(), order.getOrderId());
    }
}
