package com.example.demo.service;

import com.example.demo.model.Event;

/**
 * Analytics service interface, for processing event for analytics
 */
public interface AnalyticsService {
    /**
     * Process event data for analytics
     * @param event the event to process
     */
    void processEvent(Event event);
} 