/*
package com.example.productservice.batch;

import com.example.productservice.domain.Product;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ProductBatchConfig {

    private  JobBuilderFactory jobBuilderFactory;
    private  StepBuilderFactory stepBuilderFactory;
    private  EntityManagerFactory entityManagerFactory;
    private  JobExecutionListener jobCompletionNotificationListener;

        @Bean
    public Job importProductJob() {
        return jobBuilderFactory.get("importProductJob")
                .listener(jobCompletionNotificationListener) // Register the listener here
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Product, Product>chunk(10)
                .reader(productReader())
                .processor(productProcessor())
                .writer(productWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Product> productReader() {
        return new JpaPagingItemReaderBuilder<Product>()
                .name("productReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Product p")
                .build();
    }

    @Bean
    public ItemProcessor<Product, Product> productProcessor() {
        return product -> {
            // Process the product (for demonstration purposes, no processing here)
            return product;
        };
    }

    @Bean
    public JpaItemWriter<Product> productWriter() {
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}

*/
