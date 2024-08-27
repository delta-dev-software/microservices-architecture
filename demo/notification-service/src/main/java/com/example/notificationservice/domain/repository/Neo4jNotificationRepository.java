package com.example.notificationservice.domain.repository;


import com.example.notificationservice.domain.model.Notification;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4jNotificationRepository extends ReactiveNeo4jRepository<Notification, Long> {
}

