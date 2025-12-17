package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.validator.ValidPageable;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderClient {

    @GetMapping
    Page<OrderDto> getOrders(
            @RequestParam String username,
            @ValidPageable
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC)
            Pageable pageable);

    @PutMapping
    OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completedOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);
}