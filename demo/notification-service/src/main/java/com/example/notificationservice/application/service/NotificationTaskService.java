package com.example.notificationservice.application.service;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.core.task.TaskExecutor;

@Service
public class NotificationTaskService {

    private final TaskExecutor taskExecutor;

    public NotificationTaskService(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Async("notificationTaskExecutor")
    public void processNotificationAsync(Long notificationId) {
        // Simulate processing notification
        System.out.println("Processing notification with ID: " + notificationId);
        // Add actual notification processing logic here
    }
}

