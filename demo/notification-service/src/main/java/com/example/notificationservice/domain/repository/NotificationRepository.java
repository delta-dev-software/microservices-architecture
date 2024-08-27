package com.example.notificationservice.domain.repository;


import com.example.notificationservice.domain.model.Notification;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NotificationRepository extends ReactiveNeo4jRepository<Notification, Long> {

    // Find all notifications
    @Override
    Flux<Notification> findAll();

    // Save a notification
    @Override
    Mono<Notification> save(Notification notification);

    // Custom query method if needed
    Flux<Notification> findByMessageContaining(String message);

    Mono<Notification> findById(String id);
}


