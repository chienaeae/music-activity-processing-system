package com.example.demo.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private FlywayCallbacks flywayCallbacks;

    /**
     * Custom Flyway migration strategy
     * 
     * @return FlywayMigrationStrategy
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Add custom logic before migration
            log.info("Starting database migration...");
            
            // Perform migration
            flyway.migrate();
            
            // Add custom logic after migration
            log.info("Database migration completed");
        };
    }
    
    /**
     * Configure the Flyway instance
     * 
     * @return Flyway
     */
    @Bean(name = "flywayInitializer")
    @DependsOn("dataSource")
    public Flyway flyway() {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .outOfOrder(false)
                .locations("classpath:db/migration")
                .dataSource(new TransactionAwareDataSourceProxy(dataSource))
                .callbacks(flywayCallbacks)
                .load();
    }
} 