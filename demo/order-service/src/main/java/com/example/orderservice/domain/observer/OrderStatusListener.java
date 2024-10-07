package com.example.orderservice.domain.observer;

import com.example.orderservice.domain.Order;

public class OrderStatusListener implements OrderObserver {
    @Override
    public void onOrderUpdated(Order order) {
        System.out.println("Order updated: " + order);
    }
}

