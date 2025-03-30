package com.example.demo.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.demo.model.ActionType;
import com.example.demo.model.Event;

@Service
public class EventTrackerService {
    private final Map<ActionType, AtomicInteger> analyticsEvents = new ConcurrentHashMap<>();
    private final Map<ActionType, AtomicInteger> aiEngineEvents = new ConcurrentHashMap<>();
    private final Map<ActionType, AtomicInteger> storageEvents = new ConcurrentHashMap<>();
    private final Map<ActionType, AtomicInteger> socialEvents = new ConcurrentHashMap<>();
    
    public void trackAnalyticsEvent(Event event) {
        analyticsEvents.computeIfAbsent(event.getAction(), k -> new AtomicInteger(0))
            .incrementAndGet();
    }
    
    public void trackAiEngineEvent(Event event) {
        aiEngineEvents.computeIfAbsent(event.getAction(), k -> new AtomicInteger(0))
            .incrementAndGet();
    }
    
    public void trackStorageEvent(Event event) {
        storageEvents.computeIfAbsent(event.getAction(), k -> new AtomicInteger(0))
            .incrementAndGet();
    }
    
    public void trackSocialEvent(Event event) {
        socialEvents.computeIfAbsent(event.getAction(), k -> new AtomicInteger(0))
            .incrementAndGet();
    }
    
    public int getAnalyticsEventCount(ActionType action) {
        return analyticsEvents.getOrDefault(action, new AtomicInteger(0)).get();
    }
    
    public int getAiEngineEventCount(ActionType action) {
        return aiEngineEvents.getOrDefault(action, new AtomicInteger(0)).get();
    }
    
    public int getStorageEventCount(ActionType action) {
        return storageEvents.getOrDefault(action, new AtomicInteger(0)).get();
    }
    
    public int getSocialEventCount(ActionType action) {
        return socialEvents.getOrDefault(action, new AtomicInteger(0)).get();
    }
    
    // 為了向後兼容，保留字符串版本的方法
    public int getAnalyticsEventCount(String action) {
        return getAnalyticsEventCount(ActionType.fromString(action));
    }
    
    public int getAiEngineEventCount(String action) {
        return getAiEngineEventCount(ActionType.fromString(action));
    }
    
    public int getStorageEventCount(String action) {
        return getStorageEventCount(ActionType.fromString(action));
    }
    
    public int getSocialEventCount(String action) {
        return getSocialEventCount(ActionType.fromString(action));
    }
    
    public void reset() {
        analyticsEvents.clear();
        aiEngineEvents.clear();
        storageEvents.clear();
        socialEvents.clear();
    }
} 