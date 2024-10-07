package com.example.orderservice.infrastructure.batch;

import com.example.orderservice.domain.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/batch")
public class OrderBatchController {

    private final OrderServiceImpl orderService;

    public OrderBatchController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

   /* @PostMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        orderService.runOrderBatchJob();
        return ResponseEntity.ok("Batch job started");
    }*/
}

