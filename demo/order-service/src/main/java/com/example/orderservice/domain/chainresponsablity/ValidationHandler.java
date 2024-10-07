package com.example.orderservice.domain.chainresponsablity;

import com.example.orderservice.domain.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ValidationHandler implements OrderHandler {
    private OrderHandler next;

    @Override
    public void setNext(OrderHandler handler) {
        this.next = handler;
    }



    @Override
    public Mono<Order> handle(Order order) {
        if (order.getQuantity() <= 0) {
            return Mono.error(new RuntimeException("Invalid order quantity"));
        }

        if (next != null) {
            return next.handle(order);
        } else {
            return Mono.just(order);
        }
    }
}

