package com.example.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "user.registration.queue";
    public static final String EXCHANGE_NAME = "user.registration.exchange";
    public static final String ROUTING_KEY = "user.registration.routingKey";

    // Declare the queue
    @Bean
    public Queue userRegistrationQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange(EXCHANGE_NAME) // Example of using a DLX
                .deadLetterRoutingKey(ROUTING_KEY + ".dlq")
                //.ttl(60000) // Message TTL example
                .build();
    }

    // Declare the exchange
    @Bean
    public TopicExchange userRegistrationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Bind queue to exchange with a routing key
    @Bean
    public Binding userRegistrationBinding(Queue userRegistrationQueue, TopicExchange userRegistrationExchange) {
        return BindingBuilder.bind(userRegistrationQueue)
                .to(userRegistrationExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /*@Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }*/

    // Configure RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    // Message converter: JSON
   /* @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }*/

    // Message converter: Default (Simple)
    /*@Bean
    public MessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }*/

    // Retry template for handling message failures
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Set exponential backoff policy
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMaxInterval(10000);
        backOffPolicy.setMultiplier(2.0);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // Set retry policy
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);


        return retryTemplate;
    }

/*    // Configure SimpleMessageListenerContainer
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                                   MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        //container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(3);  // Number of consumers
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(false);  // Don't requeue failed messages
        //container.setAdviceChain(new RejectAndDontRequeueRecoverer()); // Custom error handling
        return container;
    }*/

    // Message listener adapter: Uses a method from a service class to handle messages
  /*  @Bean
    public MessageListenerAdapter listenerAdapter(MyMessageListenerService listenerService, MessageConverter converter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listenerService, "onMessage");
        adapter.setMessageConverter(converter);
        return adapter;
    }*/

    // Example of RabbitTransactionManager for handling transactions
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    // Example service class for message handling
    public static class MyMessageListenerService {
        public void onMessage(String message) {
            // Handle the message
            System.out.println("Received message: " + message);
        }
    }
}
