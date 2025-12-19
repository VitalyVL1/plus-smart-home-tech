package ru.practicum.service;

import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID orderId);

    void pickedDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    BigDecimal deliveryCost(OrderDto orderDto);

    void cancelDelivery(UUID deliveryId);
}
