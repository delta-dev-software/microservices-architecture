package com.example.productservice.messaging.listener;

import com.example.productservice.config.RabbitMQConfig;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


import com.example.productservice.config.RabbitMQConfig;
import com.example.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ProductMessageListener.class);

    private final ProductService productService;

    // Constructor injection for ProductService
    public ProductMessageListener(ProductService productService) {
        this.productService = productService;
    }

    // Listen to messages from RabbitMQ
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Object> message) {
        log.info("Received message: {}", message);

        if (message == null || message.isEmpty()) {
            log.warn("Received empty or null message");
            return;
        }

        String action = (String) message.get("action");
        Long id = (Long) message.get("id");
        String name = (String) message.get("name");
        Double price = (Double) message.get("price");

        if (action == null) {
            log.warn("Message missing 'action' field");
            return;
        }

        try {
            switch (action) {
                case "CREATE":
                    if (name != null && price != null) {
                        productService.createProduct(name, price);
                        log.info("Product created: {}", name);
                    } else {
                        log.warn("Missing product details for CREATE action");
                    }
                    break;
                case "UPDATE":
                    if (id != null && name != null && price != null) {
                        productService.updateProduct(id, name, price);
                        log.info("Product updated: ID = {}, Name = {}", id, name);
                    } else {
                        log.warn("Missing product details for UPDATE action");
                    }
                    break;
                case "DELETE":
                    if (id != null) {
                        productService.deleteProduct(id);
                        log.info("Product deleted: ID = {}", id);
                    } else {
                        log.warn("Missing product ID for DELETE action");
                    }
                    break;
                default:
                    log.warn("Unknown action: {}", action);
                    break;
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
