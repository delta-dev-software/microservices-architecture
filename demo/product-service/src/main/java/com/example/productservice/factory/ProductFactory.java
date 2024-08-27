package com.example.productservice.factory;

import com.example.productservice.domain.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;


/*
public class ProductFactory {

    public Product createProduct(String name, double price, String description) {
        // Simple factory method to create a Product instance
        return new Product(UUID.randomUUID(), name, price, description);
    }
}

*/

public interface ProductFactory {
    Product createProduct(String name, Double price, String description);
}
