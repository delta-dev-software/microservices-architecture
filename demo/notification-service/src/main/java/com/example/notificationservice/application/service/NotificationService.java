package com.example.notificationservice.service;

import com.example.notificationservice.application.service.NotificationTaskService;
import com.example.notificationservice.domain.model.Notification;
import com.example.notificationservice.domain.repository.NotificationRepository;
import com.example.notificationservice.application.dto.NotificationDTO;
import com.example.notificationservice.application.mapper.NotificationMapper;
import org.apache.camel.CamelContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTaskService notificationTaskService;
    private final NotificationMapper notificationMapper = NotificationMapper.INSTANCE;
    private final RetryTemplate retryTemplate;
    private final CamelContext camelContext;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationTaskService notificationTaskService,
                               RetryTemplate retryTemplate,
                               CamelContext camelContext) {
        this.notificationRepository = notificationRepository;
        this.notificationTaskService = notificationTaskService;
        this.retryTemplate = retryTemplate;
        this.camelContext = camelContext;
    }

    public Mono<NotificationDTO> createNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toEntity(notificationDTO);
        return notificationRepository.save(notification)
                .map(savedNotification -> {
                    notificationTaskService.processNotificationAsync(savedNotification.getId());
                    return notificationMapper.toDTO(savedNotification);
                });
    }

    public Mono<NotificationDTO> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDTO);
    }

    public Flux<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .map(notificationMapper::toDTO);
    }

    public void sendNotification(String message) throws Exception {
        retryTemplate.execute(context -> {
            performNotificationSending(message);
            return null;
        });
    }

    private void performNotificationSending(String message) throws Exception {
        // Logic for sending notification
        // This method might throw an exception and will be retried
        camelContext.createProducerTemplate().sendBody("direct:start", message);
    }
}




