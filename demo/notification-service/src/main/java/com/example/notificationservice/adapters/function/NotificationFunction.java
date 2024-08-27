package com.example.notificationservice.adapters.function;


import com.example.notificationservice.domain.model.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class NotificationFunction {

    @Bean
    public Function<Notification, Notification> markAsRead() {
        return notification -> {
            notification.setStatus("READ");
            return notification;
        };
    }
}

