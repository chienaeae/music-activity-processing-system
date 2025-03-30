package com.example.demo.service;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RawEventDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final MessageChannel eventInputChannel;
    
    public boolean sendEvent(RawEventDTO event) {
        return eventInputChannel.send(MessageBuilder.withPayload(event).build());
    }
} 