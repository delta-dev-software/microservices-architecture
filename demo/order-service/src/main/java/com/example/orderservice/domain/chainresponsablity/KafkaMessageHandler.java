package com.example.orderservice.domain.chainresponsablity;

import com.example.orderservice.domain.Order;
import com.example.orderservice.infrastructure.messaging.OrderKafkaProducer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaMessageHandler implements OrderHandler {
    private final OrderKafkaProducer orderKafkaProducer;
    private OrderHandler next;

    public KafkaMessageHandler(OrderKafkaProducer orderKafkaProducer) {
        this.orderKafkaProducer = orderKafkaProducer;
    }

    @Override
    public void setNext(OrderHandler handler) {
        this.next = handler;
    }

    @Override
    public Mono<Order> handle(Order order) {
        orderKafkaProducer.sendOrderEvent(order);

        if (next != null) {
            return next.handle(order);
        } else {
            return Mono.just(order);
        }
    }
}

