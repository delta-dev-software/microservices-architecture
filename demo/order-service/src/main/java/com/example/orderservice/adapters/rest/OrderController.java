package com.example.orderservice.adapters.rest;

import com.example.orderservice.application.dto.OrderDTO;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderService;
import com.example.orderservice.application.mapper.OrderMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<OrderDTO> createOrder(@RequestBody Order order) {
        return Mono.just(order)
                //.map(orderMapper::orderDTOToOrder)
                .flatMap(orderService::createOrder)
                .map(orderMapper::orderToOrderDTO);
    }

    @GetMapping("/{id}")
    public Mono<OrderDTO> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(orderMapper::orderToOrderDTO);
    }

    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return orderService.getAllOrders()
                .map(orderMapper::orderToOrderDTO);
    }
}
