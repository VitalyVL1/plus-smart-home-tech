package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {
    Page<OrderDto> getOrders(String username, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest request);

    OrderDto paymentSuccess(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryFailed(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateTotal(UUID orderId);

    OrderDto calculateDelivery(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);
}
