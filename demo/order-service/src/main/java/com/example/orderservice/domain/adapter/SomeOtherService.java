package com.example.orderservice.domain.adapter;

import com.example.orderservice.domain.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SomeOtherService {

    private final OrderServiceAdapter orderServiceAdapter;

    public SomeOtherService(OrderServiceAdapter orderServiceAdapter) {
        this.orderServiceAdapter = orderServiceAdapter;
    }

    public Mono<Order> handleNewOrder(Order order) {
        return orderServiceAdapter.createOrder(order);
    }
}

