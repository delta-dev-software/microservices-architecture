package com.example.orderservice.infrastructure.batch;


import com.example.orderservice.domain.Order;
import org.springframework.batch.item.ItemProcessor;

public class OrderItemProcessor implements ItemProcessor<Order, Order> {

    @Override
    public Order process(Order order) throws Exception {
        // Business logic to modify the order before saving
        order.setProcessed(true); // Example: set a flag indicating the order was processed
        return order;
    }
}

