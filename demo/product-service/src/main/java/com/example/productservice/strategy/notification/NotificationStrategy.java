package com.example.productservice.strategy.notification;

import com.example.productservice.domain.Product;

public interface NotificationStrategy {
    void notifyCreation(Product product);
}
