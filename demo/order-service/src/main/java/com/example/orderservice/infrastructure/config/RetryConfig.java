package com.example.orderservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RetryConfig {

    @Value("${custom.retry.max-attempts}")
    private int maxAttempts;

    @Value("${custom.retry.backoff-delay}")
    private long backoffDelay;

    // Getters and logic for using these properties
}

