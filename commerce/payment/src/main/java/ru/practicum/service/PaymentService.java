package ru.practicum.service;

import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto payment(OrderDto orderDto);

    BigDecimal getTotalCost(OrderDto orderDto);

    void refund(UUID paymentId);

    BigDecimal productCost(OrderDto orderDto);

    void failed(UUID paymentId);
}
