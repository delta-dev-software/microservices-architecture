package com.example.notificationservice.infrastructure.config;


import com.example.notificationservice.adapters.graphql.NotificationGraphQLController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Configuration
public class GraphQLConfig {

    private final NotificationGraphQLController graphQLController;

    public GraphQLConfig(NotificationGraphQLController graphQLController) {
        this.graphQLController = graphQLController;
    }
    private  ResourceLoader resourceLoader;

    /*@Bean
    public GraphQL graphQL() throws IOException {
        File schemaFile = new File("schema.graphqls");
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
        return GraphQL.newGraphQL(schema).build();
    }*/

    @Bean
    public GraphQL graphQL(ResourceLoader resourceLoader) throws IOException {
        // Load the schema file from the classpath
        Resource schemaResource = resourceLoader.getResource("classpath:graphql/schema.graphqls");

        try (Reader schemaReader = new InputStreamReader(schemaResource.getInputStream())) {
            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaReader);
            RuntimeWiring wiring = buildRuntimeWiring();
            SchemaGenerator schemaGenerator = new SchemaGenerator();
            GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
            return GraphQL.newGraphQL(schema).build();
        }
    }



    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWriting -> typeWriting
                        .dataFetcher("notificationById", graphQLController.getNotificationByIdFetcher())
                        .dataFetcher("allNotifications", graphQLController.getAllNotificationsFetcher()))
                .build();
    }
}

