package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.PaymentClient;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@LogAllMethods
@RestController
@RequestMapping("/api/v1/payment")
@Validated
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto payment(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.payment(orderDto);
    }

    @Override
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getTotalCost(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void refund(@RequestBody UUID paymentId) {
        paymentService.refund(paymentId);
    }

    @Override
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal productCost(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.productCost(orderDto);
    }

    @Override
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void failed(@RequestBody UUID paymentId) {
        paymentService.failed(paymentId);
    }
}
