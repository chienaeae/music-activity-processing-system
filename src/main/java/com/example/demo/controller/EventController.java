package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RawEventDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    
    private final MessageChannel eventInputChannel;
    
    @PostMapping
    public ResponseEntity<?> processEvent(@RequestBody RawEventDTO rawEvent) {
        // send the raw event to the integration flow
        boolean sent = eventInputChannel.send(MessageBuilder.withPayload(rawEvent).build());
        
        if (sent) {
            return ResponseEntity.ok().body("Event processed successfully");
        } else {
            return ResponseEntity.internalServerError().body("Failed to process event");
        }
    }
} 
