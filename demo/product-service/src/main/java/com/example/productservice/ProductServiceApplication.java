package com.example.productservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;


/**
 * Main entry point for the Spring Boot application.
 */
@SpringBootApplication
//@EnableR2dbcRepositories
//@EnableScheduling // Enables scheduling for batch jobs
@EnableJms
public class ProductServiceApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
