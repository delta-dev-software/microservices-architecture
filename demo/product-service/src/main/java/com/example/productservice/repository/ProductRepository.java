package com.example.productservice.repository;

import com.example.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import com.example.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find product by name (case insensitive)
    Optional<Product> findByNameIgnoreCase(String name);

    // Find all products with a price greater than a given amount
    List<Product> findByPriceGreaterThan(Double price);

    // Custom query using JPQL to find products by a price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findProductsInPriceRange(Double minPrice, Double maxPrice);

    // Custom query using native SQL to find top 3 expensive products
    @Query(value = "SELECT * FROM product ORDER BY price DESC LIMIT 3", nativeQuery = true)
    List<Product> findTop3ExpensiveProducts();
}

