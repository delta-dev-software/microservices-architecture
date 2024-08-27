package com.example.orderservice.infrastructure.messaging;

import com.example.orderservice.domain.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {

    @KafkaListener(topics = "order-events", groupId = "order-group",
            properties = {"spring.json.value.default.type=com.example.orderservice.domain.Order"})
    public void consumeOrderEvent(@Payload Order order) {
        // Process the consumed order event
        System.out.println("Order event consumed from Kafka: " + order.toString());
        // Further processing can be done here
    }
}
