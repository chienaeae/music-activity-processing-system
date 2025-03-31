package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.EventEntity;
import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Storage service implementation class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final EventRepository eventRepository;
    
    @Override
    public void storeEvent(Event event) {
        // Actual implementation will save event to storage system
        log.info("Saving event to storage system: {}", event);
        
        // Save event to database
        EventEntity eventEntity = EventEntity.builder()
                .userId(event.getUserId())
                .action(event.getAction())
                .songId(event.getSongId())
                .timestamp(event.getTimestamp())
                .source(event.getSource())
                .processedAt(event.getProcessedAt())
                .category(event.getAction() != null ? event.getAction().name() : "UNKNOWN")
                .count(1)
                .build();
        
        eventRepository.save(eventEntity);
        log.info("Event saved to database, ID: {}", eventEntity.getId());
    }
} 