package com.example.notificationservice.adapters.graphql;


import com.example.notificationservice.application.dto.NotificationDTO;
import com.example.notificationservice.domain.model.Notification;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationGraphQLController {

    private final com.example.notificationservice.service.NotificationService notificationService;

    public NotificationGraphQLController(com.example.notificationservice.service.NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public DataFetcher<NotificationDTO> getNotificationByIdFetcher() {
        return dataFetchingEnvironment -> {
            Long id = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return notificationService.getNotificationById(String.valueOf(id)).block();
        };
    }

    public DataFetcher<Iterable<NotificationDTO>> getAllNotificationsFetcher() {
        return dataFetchingEnvironment -> notificationService.getAllNotifications().collectList().block();
    }
}

