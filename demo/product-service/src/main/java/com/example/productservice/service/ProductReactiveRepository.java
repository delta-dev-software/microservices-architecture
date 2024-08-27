/*
package com.example.productservice.service;

import com.example.productservice.domain.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

*/
/**
 * Reactive repository interface for managing Product entities.
 *//*

public interface ProductReactiveRepository extends ReactiveCrudRepository<Product, Long> {

    */
/**
     * Finds products within a specified price range.
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A Flux containing products within the specified price range.
     *//*

    Flux<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    */
/**
     * Finds the top 3 most expensive products.
     * @return A Flux containing the top 3 most expensive products.
     *//*

    Flux<Product> findTop3ByOrderByPriceDesc();
}
*/
