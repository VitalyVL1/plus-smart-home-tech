package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryClient {
    @PutMapping
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void successfulDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void pickedDelivery(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void failedDelivery(@RequestBody UUID orderId);

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto);
}
