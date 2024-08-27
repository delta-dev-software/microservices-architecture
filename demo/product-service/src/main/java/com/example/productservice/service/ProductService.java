package com.example.productservice.service;

import com.example.productservice.domain.Product;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.factory.ProductFactory;

import com.example.productservice.factory.SimpleProductFactory;
import com.example.productservice.messaging.EventPublisher;
import com.example.productservice.messaging.ProductCreatedEvent;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.strategy.notification.NotificationStrategy;
import com.example.productservice.strategy.notification.SMSNotificationStrategy;
import com.example.productservice.strategy.price.NoDiscountPricingStrategy;
import com.example.productservice.strategy.price.PricingStrategy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private  ProductRepository productRepository;
    private SMSNotificationStrategy notificationStrategy;
    private NoDiscountPricingStrategy pricingStrategy;
    private SimpleProductFactory productFactory;
    private EventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository, SMSNotificationStrategy notificationStrategy, NoDiscountPricingStrategy pricingStrategy, SimpleProductFactory productFactory, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.notificationStrategy = notificationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.productFactory = productFactory;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackCreateProduct")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    public Product createProduct(String name, Double basePrice) {
        log.info("Creating a new product: {}", name);

        // Use the Strategy pattern to calculate the final price
        Double finalPrice = pricingStrategy.calculatePrice(basePrice);

        // Use the Factory pattern to create a product
        Product product = productFactory.createProduct(name, finalPrice, "");

        // Save the product to the repository
        Product savedProduct = productRepository.save(product);

        // Trigger the event publishing (Spring context, JMS, RabbitMQ)
        ProductCreatedEvent event = new ProductCreatedEvent(this, savedProduct);
        eventPublisher.publishEvent(event);

        // Use the Strategy pattern to send notifications
        notificationStrategy.notifyCreation(savedProduct);

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
    public Product updateProduct(Long id, String name, Double basePrice) {
        log.info("Updating product with ID: {}", id);

        // Use the Strategy pattern to calculate the final price
        Double finalPrice = pricingStrategy.calculatePrice(basePrice);

        return productRepository.findById(id).map(product -> {
            product.setName(name);
            product.setPrice(finalPrice);
            return productRepository.save(product);
        }).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
    }

    // Fallback methods for Resilience4j
    public Product fallbackCreateProduct(String name, Double basePrice, Throwable t) {
        log.error("Failed to create product, triggering fallback", t);
        return productFactory.createProduct("Fallback Product", 0.0,"");
    }

    public Optional<Product> fallbackGetProductById(Long id, Throwable t) {
        log.error("Failed to retrieve product, triggering fallback", t);
        return Optional.of(productFactory.createProduct("Fallback Product", 0.0,""));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Retrieving products with price between {} and {}", minPrice, maxPrice);

        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range parameters must not be null");
        }

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        return productRepository.findProductsInPriceRange(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Product> getTop3ExpensiveProducts() {
        log.info("Retrieving top 3 most expensive products");
        return productRepository.findTop3ExpensiveProducts();
    }
}
