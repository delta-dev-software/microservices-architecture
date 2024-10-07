package com.example.orderservice.infrastructure.search;


import com.example.orderservice.domain.Order;


import com.example.orderservice.domain.Order;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface OrderSearchRepository extends ReactiveElasticsearchRepository<Order, String> {
    Mono<Order> findById(String id);
}

