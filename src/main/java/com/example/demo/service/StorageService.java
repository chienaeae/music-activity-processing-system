package com.example.demo.service;

import com.example.demo.model.Event;

/**
 * Storage service interface, for persisting event data
 */
public interface StorageService {
    /**
     * Store event to persistent storage
     * @param event the event to store
     */
    void storeEvent(Event event);
} 