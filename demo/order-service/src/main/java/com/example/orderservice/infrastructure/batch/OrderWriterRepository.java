package com.example.orderservice.infrastructure.batch;

import com.example.orderservice.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderWriterRepository extends CrudRepository<Order, String> {
}
