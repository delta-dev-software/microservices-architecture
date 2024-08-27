package com.example.productservice.messaging.listener;

import com.example.productservice.messaging.ProductCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        // Handle the event (e.g., log it, perform some action)
        System.out.println("Handling product created event: " + event.getProduct().getName());
    }
}

