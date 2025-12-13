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

    ;

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto returnOrder(@Valid @ModelAttribute ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @Override
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentSuccess(@RequestBody UUID productId) {
        return orderService.payOrder(productId);
    }

    @Override
    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody UUID productId) {
        return orderService.paymentFailed(productId);
    }

    @Override
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody UUID productId) {
        return orderService.deliveryOrder(productId);
    }

    @Override
    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryFailed(@RequestBody UUID productId) {
        return orderService.deliveryFailed(productId);
    }

    @Override
    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto completedOrder(@RequestBody UUID productId) {
        return orderService.completedOrder(productId);
    }

    @Override
    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotal(@RequestBody UUID productId) {
        return orderService.calculateTotal(productId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateDelivery(@RequestBody UUID productId) {
        return orderService.calculateDelivery(productId);
    }

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyOrder(@RequestBody UUID productId) {
        return orderService.assemblyOrder(productId);
    }

    @Override
    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyFailed(@RequestBody UUID productId) {
        return orderService.assemblyFailed(productId);
    }
}
