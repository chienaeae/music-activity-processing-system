package com.example.demo.config;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.example.demo.dto.RawEventDTO;
import com.example.demo.model.ActionType;
import com.example.demo.model.Event;
import com.example.demo.service.EventTrackerService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {
    
    private final EventTrackerService eventTrackerService;

    @Bean
    public MessageChannel eventInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel transformedEventChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel filteredEventChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel analyticsChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel aiEngineChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel storageChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel socialChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow eventProcessingFlow() {
        return IntegrationFlow.from(eventInputChannel())
                .<RawEventDTO, Event>transform(rawEvent -> 
                    Event.builder()
                        .userId(rawEvent.getUserId())
                        .action(rawEvent.getActionType())
                        .songId(rawEvent.getSongId())
                        .timestamp(rawEvent.getTimestamp() != null ? rawEvent.getTimestamp() : Instant.now())
                        .source("WEB")
                        .processedAt(Instant.now())
                        .build())
                .channel(transformedEventChannel())
                .<Event>filter(event -> 
                    event.getUserId() != null && !event.getUserId().isEmpty() &&
                    event.getAction() != null &&
                    (event.getSongId() != null || event.getAction() == ActionType.LOGIN || event.getAction() == ActionType.LOGOUT) &&
                    !event.getUserId().startsWith("bot_"))
                .channel(filteredEventChannel())
                .route(new AbstractMessageRouter() {
                    @Override
                    protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
                        Event event = (Event) message.getPayload();
                        ActionType action = event.getAction();
                        
                        if (null == action) {
                            return List.of(storageChannel());
                        } else return switch (action) {
                            case PLAY, PAUSE, SKIP -> List.of(analyticsChannel());
                            case LIKE, DISLIKE -> List.of(analyticsChannel(), aiEngineChannel());
                            case SHARE -> List.of(analyticsChannel(), socialChannel());
                            default -> List.of(storageChannel());
                        };
                    }
                })
                .get();
    }

    @Bean
    public IntegrationFlow analyticsFlow() {
        return IntegrationFlow.from(analyticsChannel())
                .<Event>handle((payload, headers) -> {
                    System.out.println("Sending event to analytics service: " + payload.getAction() + " - " + payload.getUserId());
                    eventTrackerService.trackAnalyticsEvent(payload);
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow aiEngineFlow() {
        return IntegrationFlow.from(aiEngineChannel())
                .<Event>handle((payload, headers) -> {
                    System.out.println("Sending event to AI engine: " + payload.getAction() + " - " + payload.getUserId());
                    eventTrackerService.trackAiEngineEvent(payload);
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow storageFlow() {
        return IntegrationFlow.from(storageChannel())
                .<Event>handle((payload, headers) -> {
                    System.out.println("Sending event to storage service: " + payload.getAction() + " - " + payload.getUserId());
                    eventTrackerService.trackStorageEvent(payload);
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow socialFlow() {
        return IntegrationFlow.from(socialChannel())
                .<Event>handle((payload, headers) -> {
                    System.out.println("Sending event to social service: " + payload.getAction() + " - " + payload.getUserId());
                    eventTrackerService.trackSocialEvent(payload);
                    return null;
                })
                .get();
    }
}