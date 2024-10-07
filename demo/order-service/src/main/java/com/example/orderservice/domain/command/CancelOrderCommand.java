package com.example.orderservice.domain.command;

import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.domain.observer.OrderObserver;
import reactor.core.publisher.Mono;

import java.util.List;

public class CancelOrderCommand implements OrderCommand {

    private final String orderId;
    private final OrderRepository orderRepository;
    private final List<OrderObserver> observers;

    public CancelOrderCommand(String orderId, OrderRepository orderRepository, List<OrderObserver> observers) {
        this.orderId = orderId;
        this.orderRepository = orderRepository;
        this.observers = observers;
    }

    @Override
    public Mono<Void> execute() {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
                .flatMap(existingOrder -> {
                    existingOrder.setStatus("CANCELLED");  // Assuming there is a status field in the Order
                    return orderRepository.save(existingOrder);
                })
                .doOnSuccess(cancelledOrder -> {
                    for (OrderObserver observer : observers) {
                        observer.onOrderUpdated(cancelledOrder); // Notify observers about the cancellation
                    }
                })
                .then();
    }
}

