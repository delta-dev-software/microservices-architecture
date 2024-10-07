package com.example.orderservice.domain.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class DummyDataController {

    private final DummyDataService dummyDataService;

    @Autowired
    public DummyDataController(DummyDataService dummyDataService) {
        this.dummyDataService = dummyDataService;
    }

    // Expose an endpoint to retrieve dummy data
    @GetMapping("/fetch-dummy-data")
    public List<DummyData> fetchDummyData() {
        return dummyDataService.getDummyData();
    }
}

