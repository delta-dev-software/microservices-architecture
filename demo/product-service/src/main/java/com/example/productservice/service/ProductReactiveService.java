/*
package com.example.productservice.service;


import com.example.productservice.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReactiveService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    private  ProductReactiveRepository productReactiveRepository;

    */
/**
     * Creates a new product.
     * @param name The name of the product.
     * @param basePrice The base price of the product.
     * @return A Mono containing the created product.
     *//*

    public Mono<Product> createProduct(String name, Double basePrice) {
        log.info("Creating a new product: Name = {}, Base Price = {}", name, basePrice);
        //Product product = new Product(name, basePrice);
        Product product = new Product.ProductBuilder()
                .setName(name)
                .setPrice(basePrice)
                .build();
        return productReactiveRepository.save(product);
    }

    */
/**
     * Retrieves a product by its ID.
     * @param id The ID of the product.
     * @return A Mono containing the product, or an error if not found.
     *//*

    public Mono<Product> getProductById(Long id) {
        log.info("Retrieving product with ID: {}", id);
        return productReactiveRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product with ID " + id + " not found")));
    }

    */
/**
     * Retrieves all products.
     * @return A Flux containing all products.
     *//*

    public Flux<Product> getAllProducts() {
        log.info("Retrieving all products");
        return productReactiveRepository.findAll();
    }

    */
/**
     * Updates an existing product.
     * @param id The ID of the product to update.
     * @param name The new name of the product.
     * @param basePrice The new base price of the product.
     * @return A Mono containing the updated product, or an error if not found.
     *//*

    public Mono<Product> updateProduct(Long id, String name, Double basePrice) {
        log.info("Updating product with ID: {}. New Name = {}, New Base Price = {}", id, name, basePrice);
        return productReactiveRepository.findById(id)
                .flatMap(product -> {
                    product.setName(name);
                    product.setPrice(basePrice);
                    return productReactiveRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product with ID " + id + " not found")));
    }

    */
/**
     * Deletes a product by its ID.
     * @param id The ID of the product to delete.
     * @return A Mono that completes when the product is deleted.
     *//*

    public Mono<Void> deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        return productReactiveRepository.findById(id)
                .flatMap(product -> productReactiveRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product with ID " + id + " not found")));
    }

    */
/**
     * Retrieves products within a specified price range.
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A Flux containing products within the specified price range.
     *//*

    public Flux<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Retrieving products with price between {} and {}", minPrice, maxPrice);
        return productReactiveRepository.findByPriceBetween(minPrice, maxPrice);
    }

    */
/**
     * Retrieves the top 3 most expensive products.
     * @return A Flux containing the top 3 most expensive products.
     *//*

    public Flux<Product> getTop3ExpensiveProducts() {
        log.info("Retrieving top 3 most expensive products");
        return productReactiveRepository.findTop3ByOrderByPriceDesc();
    }
}

*/
