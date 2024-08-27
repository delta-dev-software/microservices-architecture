package com.example.productservice.service;

import com.example.productservice.domain.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.messaging.ProductCreatedEvent;
import com.example.productservice.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackCreateProduct")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    public Product createProduct(Product product) {
        log.info("Creating a new product: {}", product.getName());
        Product savedProduct = productRepository.save(product);

        // Publish a product creation event
        eventPublisher.publishEvent(new ProductCreatedEvent(this, savedProduct));

        // Send email notification about the new product creation
        emailService.sendProductCreationEmail("notify@example.com", product.getName());

        return savedProduct;
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetProductById")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    public Optional<Product> getProductById(Long id) {
        log.info("Retrieving product with ID: {}", id);
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.info("Retrieving all products");
        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        log.info("Updating product with ID: {}", id);
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return productRepository.save(product);
        }).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
    }

    // Fallback methods for Resilience4j
    public Product fallbackCreateProduct(Product product, Throwable t) {
        log.error("Failed to create product, triggering fallback", t);
        return new Product(0L, "Fallback Product", 0.0);
    }

    public Optional<Product> fallbackGetProductById(Long id, Throwable t) {
        log.error("Failed to retrieve product, triggering fallback", t);
        return Optional.of(new Product(id, "Fallback Product", 0.0));
    }
}
