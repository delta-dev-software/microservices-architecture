package com.example.notificationservice.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Define retry policy
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Max retry attempts

        retryTemplate.setRetryPolicy(retryPolicy);

        // Define backoff policy
        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy() {{
            setBackOffPeriod(2000); // Backoff period in milliseconds
        }});

        // Add custom retry listeners if needed
        retryTemplate.setListeners(new RetryListener[] {
                new RetryListener() {

                }
        });

        return retryTemplate;
    }

    // Example of using @Retryable annotations
    @Bean
    public SomeService someService() {
        return new SomeService();
    }

    public static class SomeService {

        @Retryable(
                value = { Exception.class },
                maxAttempts = 5,
                backoff = @Backoff(delay = 2000)
        )
        public void performOperation() throws Exception {
            // Method that might fail and should be retried
        }
    }
}

