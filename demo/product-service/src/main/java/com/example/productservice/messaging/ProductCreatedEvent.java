package com.example.productservice.messaging;

import com.example.productservice.domain.Product;
import org.springframework.context.ApplicationEvent;

/**
 * Event that represents the creation of a new product.
 */
import java.io.Serializable;

public class ProductCreatedEvent extends ApplicationEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Product product;

    public ProductCreatedEvent(Object source, Product product) {
        super(source);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}

