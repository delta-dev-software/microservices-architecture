package com.example.orderservice.infrastructure.batch;



import com.example.orderservice.domain.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReaderRepository extends PagingAndSortingRepository<Order, String> {
    // You can define custom query methods if needed
}

