package com.example.demo.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestDatabaseConfig {
    @Container
    private static final MSSQLServerContainer<?> sqlServerContainer = new MSSQLServerContainer<>(
            DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest"))
            .withPassword("YourStrong@Passw0rd")
            .withUrlParam("trustServerCertificate", "true");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        sqlServerContainer.start();
        registry.add("spring.datasource.url", () -> sqlServerContainer.getJdbcUrl() + ";trustServerCertificate=true");
        registry.add("spring.datasource.username", () -> sqlServerContainer.getUsername());
        registry.add("spring.datasource.password", () -> sqlServerContainer.getPassword());
        
        // create-drop mode to automatically create table structure
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        
        // ensure Flyway can execute migrations
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.baseline-on-migrate", () -> "true");
    }
} 