package com.example.orderservice.domain;

import com.example.orderservice.infrastructure.messaging.OrderKafkaProducer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderKafkaProducer orderKafkaProducer;


    public OrderServiceImpl(OrderRepository orderRepository, OrderKafkaProducer orderKafkaProducer) {
        this.orderRepository = orderRepository;
        this.orderKafkaProducer = orderKafkaProducer;
    }

    public Mono<Order> createOrder(Order order) {
        return orderRepository.save(order)
                .doOnSuccess(savedOrder -> orderKafkaProducer.sendOrderEvent(savedOrder));
    }

    @Override
    public Mono<Order> getOrderById(String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }

    @Override
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

