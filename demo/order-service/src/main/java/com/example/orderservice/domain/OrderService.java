package com.example.orderservice.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {
    Mono<Order> createOrder(Order order);
    Mono<Order> getOrderById(String id);
    Flux<Order> getAllOrders();
}

