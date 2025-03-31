package com.example.demo.integration;

import java.time.Instant;
import java.util.List;

import org.flywaydb.core.Flyway;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.demo.config.TestDatabaseConfig;
import com.example.demo.dto.RawEventDTO;
import com.example.demo.entity.EventEntity;
import com.example.demo.model.ActionType;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.AiEngineService;
import com.example.demo.service.AnalyticsService;
import com.example.demo.service.EventService;
import com.example.demo.service.SocialService;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class EventIntegrationTest extends TestDatabaseConfig {

    @Autowired
    private EventService eventService;

    @Autowired
    private Flyway flyway;

    @MockitoBean
    private AnalyticsService analyticsService;

    @MockitoBean
    private AiEngineService aiEngineService;

    @MockitoBean
    private SocialService socialService;

    @Autowired
    private EventRepository eventRepository;
    
    // Only execute Flyway migration once before the test
    private static boolean migrationApplied = false;
    
    @BeforeEach
    public void setup() {
        // Only execute Flyway migration once before the test
        if (!migrationApplied) {
            try {
                // Execute migration directly, without cleaning the database
                flyway.migrate();
                migrationApplied = true;
                System.out.println("Flyway migrations applied successfully");
            } catch (Exception e) {
                System.err.println("Error during Flyway migration: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Clear the events table for testing
        eventRepository.deleteAll();
    }
    
    @Test
    public void testPlayEvent() {
        // Create test event
        RawEventDTO event = new RawEventDTO();
        event.setUserId("user123");
        event.setAction("play");
        event.setSongId("song456");
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify event was sent to correct services
        Mockito.verify(analyticsService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.times(1)).processEvent(Mockito.any());
        
        // Verify the event was stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findByUserIdAndAction("user123", ActionType.PLAY);
        assertEquals(1, events.size(), "There should be one PLAY event stored");
        assertEquals("song456", events.get(0).getSongId());
        assertEquals("PLAY", events.get(0).getCategory());
    }
    
    @Test
    public void testLikeEvent() {
        // Create test event
        RawEventDTO event = new RawEventDTO();
        event.setUserId("user123");
        event.setAction("like");
        event.setSongId("song456");
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify event was sent to correct services
        Mockito.verify(analyticsService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(socialService, Mockito.times(1)).shareEvent(Mockito.any());
        
        // Verify the event was stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findByUserIdAndAction("user123", ActionType.LIKE);
        assertEquals(1, events.size(), "There should be one LIKE event stored");
        assertEquals("song456", events.get(0).getSongId());
        assertEquals("LIKE", events.get(0).getCategory());
    }
    
    @Test
    public void testShareEvent() {
        // Create test event
        RawEventDTO event = new RawEventDTO();
        event.setUserId("user123");
        event.setAction("share");
        event.setSongId("song456");
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify event was sent to correct services
        Mockito.verify(analyticsService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(socialService, Mockito.times(1)).shareEvent(Mockito.any());
        
        // Verify the event was stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findByUserIdAndAction("user123", ActionType.SHARE);
        assertEquals(1, events.size(), "There should be one SHARE event stored");
        assertEquals("song456", events.get(0).getSongId());
        assertEquals("SHARE", events.get(0).getCategory());
    }
    
    @Test
    public void testLoginEvent() {
        // Create test event
        RawEventDTO event = new RawEventDTO();
        event.setUserId("user123");
        event.setAction("login");
        event.setSongId(null); // No song for login events
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify event was sent to correct services
        Mockito.verify(analyticsService, Mockito.times(1)).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.never()).processEvent(Mockito.any());
        Mockito.verify(socialService, Mockito.never()).shareEvent(Mockito.any());
        
        // Verify the event was stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findByUserIdAndAction("user123", ActionType.LOGIN);
        assertEquals(1, events.size(), "There should be one LOGIN event stored");
        assertNotNull(events.get(0).getProcessedAt(), "Processed time should be recorded");
        assertEquals("LOGIN", events.get(0).getCategory());
    }
    
    @Test
    public void testFilteringInvalidEvent() {
        // Create test event with null userId (should be filtered out)
        RawEventDTO event = new RawEventDTO();
        event.setUserId(null);
        event.setAction("play");
        event.setSongId("song456");
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify no services received the event
        Mockito.verify(analyticsService, Mockito.never()).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.never()).processEvent(Mockito.any());
        Mockito.verify(socialService, Mockito.never()).shareEvent(Mockito.any());
        
        // Verify no events were stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findAll();
        assertEquals(0, events.size(), "There should be no events stored");
    }
    
    @Test
    public void testBotEventsFiltered() {
        // Create test event with bot userId (should go only to storage)
        RawEventDTO event = new RawEventDTO();
        event.setUserId("bot_crawler");
        event.setAction("play");
        event.setSongId("song456");
        event.setTimestamp(Instant.now());

        // Send the event
        eventService.sendEvent(event);

        // Verify only storage received the event
        Mockito.verify(analyticsService, Mockito.never()).processEvent(Mockito.any());
        Mockito.verify(aiEngineService, Mockito.never()).processEvent(Mockito.any());
        Mockito.verify(socialService, Mockito.never()).shareEvent(Mockito.any());
        
        // Verify the event was stored in the database
        sleep(1000); // Give some time for asynchronous processing
        
        List<EventEntity> events = eventRepository.findByUserIdAndAction("bot_crawler", ActionType.PLAY);
        assertEquals(1, events.size(), "There should be one PLAY event stored for bot");
        assertEquals("song456", events.get(0).getSongId());
        assertEquals("PLAY", events.get(0).getCategory());
    }
    
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 