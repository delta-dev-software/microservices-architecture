package com.example.notificationservice.infrastructure.config;


import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.AuthTokens;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.repository.config.Neo4jRepositoryConfigurationExtension;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.example.notificationservice.domain.repository")
public class Neo4jConfig  {

    @Bean
    public Driver neo4jDriver() {
        return GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
    }

    @Bean
    public Neo4jMappingContext neo4jMappingContext() {
        return new Neo4jMappingContext();
    }

    @Bean
    public Neo4jRepositoryConfigurationExtension neo4jRepositoryConfigurationExtension() {
        return new Neo4jRepositoryConfigurationExtension();
    }
}

