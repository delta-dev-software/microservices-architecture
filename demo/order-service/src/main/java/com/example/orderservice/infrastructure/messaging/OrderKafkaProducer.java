package com.example.orderservice.infrastructure.messaging;

import com.example.orderservice.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer {

    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, Order> kafkaTemplate;


    public OrderKafkaProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(Order order) {
        kafkaTemplate.send(TOPIC, order.getId(), order);
        System.out.println("Order event sent to Kafka: " + order.getId());
    }
}

