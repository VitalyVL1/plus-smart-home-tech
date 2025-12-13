package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.DeliveryClient;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@LogAllMethods
@RestController
@RequestMapping("/api/v1/delivery")
@Validated
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    public void successfulDelivery(@RequestBody UUID orderId) {
        deliveryService.successfulDelivery(orderId);
    }

    @Override
    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.OK)
    public void pickedDelivery(@RequestBody UUID orderId) {
        deliveryService.pickedDelivery(orderId);
    }

    @Override
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void failedDelivery(@RequestBody UUID orderId) {
        deliveryService.failedDelivery(orderId);
    }

    @Override
    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto) {
        return deliveryService.deliveryCost(orderDto);
    }
}
