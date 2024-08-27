package com.example.notificationservice.adapters.rest;


import com.example.notificationservice.application.dto.NotificationDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final com.example.notificationservice.service.NotificationService notificationService;

    public NotificationController(com.example.notificationservice.service.NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Mono<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.createNotification(notificationDTO);
    }

    @GetMapping("/{id}")
    public Mono<NotificationDTO> getNotificationById(@PathVariable String id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping
    public Flux<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}

