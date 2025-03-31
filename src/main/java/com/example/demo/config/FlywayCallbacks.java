package com.example.demo.config;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Flyway callback class, for custom logic during migration phases
 */
@Slf4j
@Component
public class FlywayCallbacks implements Callback {

    @Override
    public boolean supports(Event event, Context context) {
        // Select the events to handle
        return event == Event.BEFORE_MIGRATE 
            || event == Event.AFTER_MIGRATE
            || event == Event.BEFORE_EACH_MIGRATE
            || event == Event.AFTER_EACH_MIGRATE;
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        switch (event) {
            case BEFORE_MIGRATE:
                log.info("Flyway migration started, before callback...");
                // Perform preparation work before migration
                break;
            case AFTER_MIGRATE:
                log.info("Flyway migration completed, after callback...");
                // Perform cleanup work after migration
                break;
            case BEFORE_EACH_MIGRATE:
                log.info("Migration started: {}", context.getMigrationInfo().getDescription());
                break;
            case AFTER_EACH_MIGRATE:
                log.info("Migration completed: {} (duration: {}ms)", 
                        context.getMigrationInfo().getDescription(),
                        context.getMigrationInfo().getExecutionTime());
                break;
            default:
                break;
        }
    }

    @Override
    public String getCallbackName() {
        return FlywayCallbacks.class.getSimpleName();
    }
} 