package com.example.orderservice.domain;

import com.example.orderservice.domain.chainresponsablity.KafkaMessageHandler;
import com.example.orderservice.domain.chainresponsablity.LoggingHandler;
import com.example.orderservice.domain.chainresponsablity.OrderHandler;
import com.example.orderservice.domain.chainresponsablity.ValidationHandler;
import com.example.orderservice.domain.command.CancelOrderCommand;
import com.example.orderservice.domain.command.OrderCommand;
import com.example.orderservice.domain.observer.OrderObserver;
import com.example.orderservice.domain.observer.OrderStatusListener;
import com.example.orderservice.infrastructure.messaging.OrderKafkaProducer;
import com.example.orderservice.infrastructure.search.OrderSearchRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
@Lazy
@Primary
//@Conditional(OrderSearchRepositoryCondition.class)
@DependsOn({"orderKafkaProducer"})
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    private final OrderRepository orderRepository;
    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderSearchRepository orderSearchRepository;
    private final List<OrderObserver> observers = new ArrayList<>();
    private final OrderHandler orderHandlerChain;
    //private final JobLauncher jobLauncher;
    //private final Job orderJob;


    public OrderServiceImpl(OrderRepository orderRepository, OrderKafkaProducer orderKafkaProducer, OrderSearchRepository orderSearchRepository, LoggingHandler loggingHandler,
                            ValidationHandler validationHandler,
                            KafkaMessageHandler kafkaMessageHandler) {
        this.orderRepository = orderRepository;
        this.orderKafkaProducer = orderKafkaProducer;
        this.orderSearchRepository = orderSearchRepository;
        addObserver(new OrderStatusListener());
        loggingHandler.setNext(validationHandler);
        validationHandler.setNext(kafkaMessageHandler);
        this.orderHandlerChain = loggingHandler;
        //this.jobLauncher=jobLauncher;// Start of the chain
        //this.orderJob=orderJob;
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @PostConstruct
    public void init() {
        // Initialization logic
        System.out.println("OrderService initialized");
    }

    @PreDestroy
    public void destroy() {
        // Cleanup logic
        System.out.println("OrderService is being destroyed");
    }

    //@Async
    public Mono<Order> createOrder(Order order) {
        return orderSearchRepository.save(order)
        //return orderRepository.save(order)
                .doOnSuccess(savedOrder -> orderKafkaProducer.sendOrderEvent(savedOrder));
    }

    @Override
    public Mono<Order> getOrderById(String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof RuntimeException));

    }

    @Override
    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public Flux<Order> getAllOrders() {
       // return orderRepository.findAll();
        return orderSearchRepository.findAll();
    }

    private void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.onOrderUpdated(order);
        }
    }

    @Override
    public Mono<Order> updateOrder(String id, Order order) {
        logger.info("Updating order with ID: {}", id);

        return orderRepository.findById(id) // Find the existing order
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found"))) // Handle case where order is not found
                .flatMap(existingOrder -> {
                    // Update fields in the existing order with the new data
                    existingOrder.setCustomerId(order.getCustomerId());
                    existingOrder.setQuantity(9);

                    // Save the updated order
                    return orderHandlerChain.handle(existingOrder)
                            .flatMap(orderRepository::save);
                })
                .doOnSuccess(updatedOrder -> {
                    logger.info("Order updated successfully: {}", updatedOrder);
                    notifyObservers(updatedOrder); // Notify observers
                })
                .doOnError(error -> logger.error("Failed to update order", error));
    }

    @Override
    public Mono<Void> deleteOrderById(String id) {
        logger.info("Deleting order with ID: {}", id);

        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found"))) // Handle case where order is not found
                .flatMap(existingOrder -> orderRepository.delete(existingOrder))
                .doOnSuccess(unused -> logger.info("Order deleted successfully: {}", id))
                .doOnError(error -> logger.error("Failed to delete order with ID: {}", id, error));
    }

    public Mono<Order> duplicateOrder(String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
                .map(Order::clone) // Use prototype pattern to clone the order
                .flatMap(clonedOrder -> {
                    clonedOrder.setId(null); // Generate a new ID for the cloned order
                    return orderRepository.save(clonedOrder); // Save the cloned order as a new one
                })
                .doOnSuccess(savedOrder -> logger.info("Order duplicated successfully: {}", savedOrder))
                .doOnError(error -> logger.error("Failed to duplicate order", error));
    }

    public Mono<Void> cancelOrder(String orderId) {
        OrderCommand cancelOrderCommand = new CancelOrderCommand(orderId, orderRepository, observers);
        return cancelOrderCommand.execute();
    }

   /* public void runOrderBatchJob() {
        try {
            jobLauncher.run(orderJob, new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters());
            logger.info("Order batch job started successfully");
        } catch (JobExecutionAlreadyRunningException | JobRestartException e) {
            logger.error("Failed to start order batch job", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }*/

}

