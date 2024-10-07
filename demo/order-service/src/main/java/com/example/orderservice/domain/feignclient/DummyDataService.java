package com.example.orderservice.domain.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DummyDataService {

    private final DummyDataClient dummyDataClient;

    @Autowired
    public DummyDataService(DummyDataClient dummyDataClient) {
        this.dummyDataClient = dummyDataClient;
    }

    // Method to call Feign client and retrieve dummy data
    public List<DummyData> getDummyData() {
        return dummyDataClient.getDummyData();
    }
}

