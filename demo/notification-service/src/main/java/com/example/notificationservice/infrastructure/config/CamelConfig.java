package com.example.notificationservice.infrastructure.config;



import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Example route: Direct to Log
              /*  from("direct:start")
                        .log("Received message: ${body}")
                        .to("log:com.example.notificationservice?level=INFO");*/

                // Example route: File Transfer
                from("file:inputFolder?noop=true")
                        .to("file:outputFolder")
                        .log("File moved from inputFolder to outputFolder");

                // Example route: HTTP Endpoint to Kafka
              /*  from("http://localhost:8080/api/notifications")
                        .log("Received HTTP request: ${body}")
                        .to("kafka:notificationsTopic")
                        .log("Message sent to Kafka topic: notificationsTopic");*/

                // Example route: Timer to Log
                from("timer:myTimer?period=10000")
                        .log("Timer triggered: ${body}");
            }
        };
    }

    @Bean
    public CamelContextConfiguration camelContextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                // Initialize Camel components or set properties before the context starts
                System.out.println("CamelContext is about to start...");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // Actions to take after the Camel context starts
                System.out.println("CamelContext has started.");
            }
        };
    }
}


