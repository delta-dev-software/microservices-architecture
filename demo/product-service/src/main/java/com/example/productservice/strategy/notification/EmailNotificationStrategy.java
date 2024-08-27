package com.example.productservice.strategy.notification;

import com.example.productservice.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void notifyCreation(Product product) {
        // Logic for sending an email
        System.out.println("Sending email notification for product: " + product.getName());
    }
}
