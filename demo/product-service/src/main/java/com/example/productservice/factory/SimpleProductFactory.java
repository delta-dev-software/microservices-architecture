package com.example.productservice.factory;

import com.example.productservice.domain.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SimpleProductFactory implements ProductFactory {
    @Override
    public Product createProduct(String name, Double price, String description) {
        return new Product(UUID.randomUUID(), name, price, description);
    }
}
