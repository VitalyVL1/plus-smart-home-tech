package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.OrderClient;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.service.OrderService;

import java.util.UUID;

@LogAllMethods
@RestController
@RequestMapping("/api/v1/order")
@Validated
@RequiredArgsConstructor
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDto> getOrders(
            @RequestParam String username,
            @RequestParam(required = false)
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return orderService.getOrders(username, pageable);
    }


    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @Override
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentSuccess(@RequestBody UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @Override
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody UUID orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    @Override
    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotal(@RequestBody UUID orderId) {
        return orderService.calculateTotal(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateDelivery(@RequestBody UUID orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}
