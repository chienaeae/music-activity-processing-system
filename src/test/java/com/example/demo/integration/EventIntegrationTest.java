package com.example.demo.integration;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.RawEventDTO;
import com.example.demo.model.ActionType;
import com.example.demo.service.EventService;
import com.example.demo.service.EventTrackerService;

@SpringBootTest
public class EventIntegrationTest {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private EventTrackerService eventTrackerService;
    
    @BeforeEach
    public void setup() {
        eventTrackerService.reset();
    }
    
    @Test
    public void testPlayEvent() {
        // prepare test data
        RawEventDTO event = RawEventDTO.builder()
                .userId("user123")
                .action("play")
                .songId("song456")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results
        assertEquals(1, eventTrackerService.getAnalyticsEventCount(ActionType.PLAY), 
                "PLAY event should be sent to analytics service");
        assertEquals(0, eventTrackerService.getAiEngineEventCount(ActionType.PLAY), 
                "PLAY event should not be sent to AI engine");
        assertEquals(0, eventTrackerService.getSocialEventCount(ActionType.PLAY), 
                "PLAY event should not be sent to social service");
        assertEquals(0, eventTrackerService.getStorageEventCount(ActionType.PLAY), 
                "PLAY event should not be sent to storage service");
    }
    
    @Test
    public void testLikeEvent() {
        // prepare test data
        RawEventDTO event = RawEventDTO.builder()
                .userId("user123")
                .action("like")
                .songId("song456")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results
        assertEquals(1, eventTrackerService.getAnalyticsEventCount(ActionType.LIKE), 
                "LIKE event should be sent to analytics service");
        assertEquals(1, eventTrackerService.getAiEngineEventCount(ActionType.LIKE), 
                "LIKE event should be sent to AI engine");
        assertEquals(0, eventTrackerService.getSocialEventCount(ActionType.LIKE), 
                "LIKE event should not be sent to social service");
        assertEquals(0, eventTrackerService.getStorageEventCount(ActionType.LIKE), 
                "LIKE event should not be sent to storage service");
    }
    
    @Test
    public void testShareEvent() {
        // prepare test data
        RawEventDTO event = RawEventDTO.builder()
                .userId("user123")
                .action("share")
                .songId("song456")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results
        assertEquals(1, eventTrackerService.getAnalyticsEventCount(ActionType.SHARE), 
                "SHARE event should be sent to analytics service");
        assertEquals(0, eventTrackerService.getAiEngineEventCount(ActionType.SHARE), 
                "SHARE event should not be sent to AI engine");
        assertEquals(1, eventTrackerService.getSocialEventCount(ActionType.SHARE), 
                "SHARE event should be sent to social service");
        assertEquals(0, eventTrackerService.getStorageEventCount(ActionType.SHARE), 
                "SHARE event should not be sent to storage service");
    }
    
    @Test
    public void testLoginEvent() {
        // prepare test data
        RawEventDTO event = RawEventDTO.builder()
                .userId("user123")
                .action("login")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results - LOGIN should be sent to storage service
        assertEquals(0, eventTrackerService.getAnalyticsEventCount(ActionType.LOGIN), 
                "LOGIN event should not be sent to analytics service");
        assertEquals(0, eventTrackerService.getAiEngineEventCount(ActionType.LOGIN), 
                "LOGIN event should not be sent to AI engine");
        assertEquals(0, eventTrackerService.getSocialEventCount(ActionType.LOGIN), 
                "LOGIN event should not be sent to social service");
        assertEquals(1, eventTrackerService.getStorageEventCount(ActionType.LOGIN), 
                "LOGIN event should be sent to storage service");
    }
    
    @Test
    public void testFilteringInvalidEvent() {
        // prepare test data - no userId
        RawEventDTO event = RawEventDTO.builder()
                .action("play")
                .songId("song456")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results - event should be filtered out
        assertEquals(0, eventTrackerService.getAnalyticsEventCount(ActionType.PLAY), 
                "Invalid event should be filtered out");
        assertEquals(0, eventTrackerService.getAiEngineEventCount(ActionType.PLAY), 
                "Invalid event should be filtered out");
        assertEquals(0, eventTrackerService.getSocialEventCount(ActionType.PLAY), 
                "Invalid event should be filtered out");
        assertEquals(0, eventTrackerService.getStorageEventCount(ActionType.PLAY), 
                "Invalid event should be filtered out");
    }
    
    @Test
    public void testBotEventsFiltered() {
        // prepare test data - bot user
        RawEventDTO event = RawEventDTO.builder()
                .userId("bot_123")
                .action("play")
                .songId("song456")
                .timestamp(Instant.now())
                .build();
        
        // send event
        boolean sent = eventService.sendEvent(event);
        assertTrue(sent, "Event should be sent successfully");
        
        // wait for event processing
        sleep(500);
        
        // verify results - event should be filtered out
        assertEquals(0, eventTrackerService.getAnalyticsEventCount(ActionType.PLAY), 
                "bot event should be filtered out");
    }
    
    // helper method, for waiting
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 