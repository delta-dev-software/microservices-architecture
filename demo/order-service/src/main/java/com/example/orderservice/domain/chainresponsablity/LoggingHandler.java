package com.example.orderservice.domain.chainresponsablity;

import com.example.orderservice.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
public class LoggingHandler implements OrderHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingHandler.class);
    private OrderHandler next;

    @Override
    public void setNext(OrderHandler handler) {
        this.next = handler;
    }



    @Override
    public Mono<Order> handle(Order order) {
        logger.info("Processing order with ID: {}", order.getId());

        if (next != null) {
            return next.handle(order);
        } else {
            return Mono.just(order);
        }
    }
}

