package com.example.productservice.strategy.notification;

import com.example.productservice.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public void notifyCreation(Product product) {
        // Logic for sending an SMS
        System.out.println("Sending SMS notification for product: " + product.getName());
    }
}
