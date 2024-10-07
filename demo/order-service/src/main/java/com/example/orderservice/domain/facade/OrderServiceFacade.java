package com.example.orderservice.domain.facade;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceFacade {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderServiceFacade(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    public Mono<Order> createOrder(Order order) {
        return orderService.createOrder(order);
    }

    public Mono<Order> getOrderById(String id) {
        return orderService.getOrderById(id);
    }

    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public Mono<Order> updateOrder(String id, Order order) {
        return orderService.updateOrder(id, order);
    }

    public Mono<Void> deleteOrderById(String id) {
        return orderService.deleteOrderById(id);
    }

    public Mono<Order> duplicateOrder(String id) {
        return orderService.duplicateOrder(id);
    }
}

