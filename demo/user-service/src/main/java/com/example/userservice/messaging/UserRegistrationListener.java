package com.example.userservice.messaging;

import com.example.userservice.config.RabbitMQConfig;
import com.example.userservice.domain.User;
import com.example.userservice.dto.UserRegistrationMessage;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleUserRegistration(UserRegistrationMessage message) {
        // Process the received message
        System.out.println("Received User Registration Message:");
        System.out.println("Name: " + message.getName());
        System.out.println("Email: " + message.getEmail());
        System.out.println("Password: " + message.getPassword());

        // Call userService or any other processing logic
       // userService.registerUser(message.getName(), message.getEmail(), message.getPassword());
    }
}
