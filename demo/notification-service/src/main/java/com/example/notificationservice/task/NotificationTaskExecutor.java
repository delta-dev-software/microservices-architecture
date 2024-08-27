package com.example.notificationservice.task;
/*

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificationTaskExecutor {

    private final com.example.notificationservice.service.NotificationService notificationService;

    public NotificationTaskExecutor(com.example.notificationservice.service.NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void executeTask(String notificationId) {
        // Ensure notificationId is not null
        if (notificationId != null) {
            notificationService.getNotificationById(notificationId)
                    .doOnNext(notificationDTO -> {
                        // Process the notification here
                        System.out.println("Processing notification: " + notificationDTO.getMessage());
                    })
                    .doOnError(error -> {
                        // Handle the error case
                        System.err.println("Error processing notification: " + error.getMessage());
                    })
                    .subscribe();
        } else {
            System.err.println("Notification ID is null");
        }
    }
}*/

