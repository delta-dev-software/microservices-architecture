/*
package com.example.orderservice.infrastructure.batch;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.infrastructure.batch.OrderItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class OrderBatchConfig {

    private final OrderReaderRepository orderRepository;
    private final OrderWriterRepository orderWriterRepository;

    public OrderBatchConfig(OrderReaderRepository orderRepository, OrderWriterRepository orderWriterRepository) {
        this.orderRepository = orderRepository;
        this.orderWriterRepository = orderWriterRepository;
    }

    @Bean
    public RepositoryItemReader<Order> reader() {
        RepositoryItemReader<Order> reader = new RepositoryItemReader<>();
        reader.setRepository(orderRepository);
        reader.setMethodName("findAll"); // This method will read all orders
        reader.setSort(Collections.emptyMap());
        return reader;
    }

    @Bean
    public OrderItemProcessor processor() {
        return new OrderItemProcessor(); // Business logic for processing each order
    }

    @Bean
    public RepositoryItemWriter<Order> writer() {
        RepositoryItemWriter<Order> writer = new RepositoryItemWriter<>();
        writer.setRepository(orderWriterRepository);
        writer.setMethodName("save"); // Write the processed order back to repository
        return writer;
    }

    @Bean
    public Step orderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("orderStep", jobRepository)
                .<Order, Order>chunk(10, transactionManager) // Process 10 orders at a time
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job orderJob(JobRepository jobRepository, Step orderStep) {
        return new JobBuilder("orderJob", jobRepository)
                .start(orderStep)
                .build();
    }
}
*/
