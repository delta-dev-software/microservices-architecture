/*
package com.example.productservice.controller;

import com.example.productservice.domain.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.service.ProductReactiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactive/products")
@RequiredArgsConstructor
public class ProductReactiveController {

    private  ProductReactiveService productReactiveService;

    // Create a new product
    @PostMapping
    public Mono<ResponseEntity<Product>> createProduct(@RequestBody Product productRequest) {
        return productReactiveService.createProduct(productRequest.getName(), productRequest.getBasePrice())
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        return productReactiveService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Get all products
    @GetMapping
    public Flux<Product> getAllProducts() {
        return productReactiveService.getAllProducts();
    }

    // Update an existing product
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productRequest) {
        return productReactiveService.updateProduct(id, productRequest.getName(), productRequest.getBasePrice())
                .map(product -> ResponseEntity.ok(product))
                .onErrorResume(ProductNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteProduct(@PathVariable Long id) {
        return productReactiveService.deleteProduct(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(ProductNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Get products by price range
    @GetMapping("/price-range")
    public Flux<Product> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return productReactiveService.getProductsByPriceRange(minPrice, maxPrice);
    }

    // Get top 3 expensive products
    @GetMapping("/top-expensive")
    public Flux<Product> getTop3ExpensiveProducts() {
        return productReactiveService.getTop3ExpensiveProducts();
    }
}
*/
