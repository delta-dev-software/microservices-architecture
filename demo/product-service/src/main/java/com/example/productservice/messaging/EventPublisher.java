package com.example.productservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final JmsTemplate jmsTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventPublisher(ApplicationEventPublisher applicationEventPublisher,
                          JmsTemplate jmsTemplate,
                          RabbitTemplate rabbitTemplate) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.jmsTemplate = jmsTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Publish event using Spring's ApplicationEventPublisher
    public void publishEvent(ProductCreatedEvent event) {
        // Publish the event within the Spring context
        applicationEventPublisher.publishEvent(event);
        System.out.println("Event published within Spring context: " + event.getProduct().getName());

        // Additionally, publish the event to JMS
        publishToJMS(event);

        // Additionally, publish the event to RabbitMQ
        publishToRabbitMQ(event);
    }

    // Publish event to JMS queue
    private void publishToJMS(ProductCreatedEvent event) {
        jmsTemplate.convertAndSend("product.created.queue", event);
        System.out.println("Event sent to JMS queue: " + event.getProduct().getName());
    }

    // Publish event to RabbitMQ exchange
    private void publishToRabbitMQ(ProductCreatedEvent event) {
        rabbitTemplate.convertAndSend("product.queue", event);
        System.out.println("Event sent to RabbitMQ: " + event.getProduct().getName());
    }
}

