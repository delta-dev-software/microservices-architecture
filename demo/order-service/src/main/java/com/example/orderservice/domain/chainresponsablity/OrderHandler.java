package com.example.orderservice.domain.chainresponsablity;

import com.example.orderservice.domain.Order;
import reactor.core.publisher.Mono;

public interface OrderHandler {
    void setNext(OrderHandler handler);
    Mono<Order> handle(Order order);
}

