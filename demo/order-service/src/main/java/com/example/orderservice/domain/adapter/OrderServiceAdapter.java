package com.example.orderservice.domain.adapter;

import com.example.orderservice.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderServiceAdapter {
    Mono<Order> createOrder(Order order);
    Mono<Order> getOrderById(String id);
    Flux<Order> getAllOrders();
    Mono<Order> updateOrder(String id, Order order);
    Mono<Void> deleteOrderById(String id);
    Mono<Order> duplicateOrder(String id);
}

