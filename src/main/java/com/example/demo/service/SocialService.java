package com.example.demo.service;

import com.example.demo.model.Event;

/**
 * Social service interface, for processing event for social sharing
 */
public interface SocialService {
    /**
     * Share event to social media
     * @param event the event to share
     */
    void shareEvent(Event event);
} 