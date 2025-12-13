package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentClient {
    @PostMapping
    PaymentDto payment(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/totalCost")
    BigDecimal getTotalCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/refund")
    void refund(@RequestBody UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal productCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/failed")
    void failed(@RequestBody UUID paymentId);
}
