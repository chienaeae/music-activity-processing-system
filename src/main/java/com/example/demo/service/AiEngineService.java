package com.example.demo.service;

import com.example.demo.model.Event;

/**
 * AI engine service interface, for processing event for AI analysis
 */
public interface AiEngineService {
    /**
     * Process event data for AI analysis
     * @param event the event to process
     */
    void processEvent(Event event);
} 