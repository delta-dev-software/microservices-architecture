package com.example.orderservice.domain.observer;

import com.example.orderservice.domain.Order;

public interface OrderObserver {
    void onOrderUpdated(Order order);
}

