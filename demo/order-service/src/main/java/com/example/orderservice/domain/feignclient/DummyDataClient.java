package com.example.orderservice.domain.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

// Define a Feign Client to query external service
@FeignClient(name = "dummyDataClient", url = "https://jsonplaceholder.typicode.com")
public interface DummyDataClient {

    // Mapping to the endpoint to get data
    @GetMapping("/posts")
    List<DummyData> getDummyData();
}
