package com.example.notificationservice.adapters.camel;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:start")
                .log("Received notification: ${body}");
    }
}

