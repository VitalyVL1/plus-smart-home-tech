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

    OrderDto payOrder(UUID productId);

    OrderDto paymentFailed(UUID productId);

    OrderDto deliveryOrder(UUID productId);

    OrderDto deliveryFailed(UUID productId);

    OrderDto completedOrder(UUID productId);

    OrderDto calculateTotal(UUID productId);

    OrderDto calculateDelivery(UUID productId);

    OrderDto assemblyOrder(UUID productId);

    OrderDto assemblyFailed(UUID productId);
}
