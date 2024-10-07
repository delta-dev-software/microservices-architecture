package com.example.orderservice.domain.adapter;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderServiceImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceAdapterImpl implements OrderServiceAdapter {

    private final OrderServiceImpl orderServiceImpl;

    public OrderServiceAdapterImpl(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @Override
    public Mono<Order> createOrder(Order order) {
        return orderServiceImpl.createOrder(order);
    }

    @Override
    public Mono<Order> getOrderById(String id) {
        return orderServiceImpl.getOrderById(id);
    }

    @Override
    public Flux<Order> getAllOrders() {
        return orderServiceImpl.getAllOrders();
    }

    @Override
    public Mono<Order> updateOrder(String id, Order order) {
        return orderServiceImpl.updateOrder(id, order);
    }

    @Override
    public Mono<Void> deleteOrderById(String id) {
        return orderServiceImpl.deleteOrderById(id);
    }

    @Override
    public Mono<Order> duplicateOrder(String id) {
        return orderServiceImpl.duplicateOrder(id);
    }
}

