package com.example.demo.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String userId;
    private ActionType action;
    private String songId;
    private Instant timestamp;
    private String source;
    private Instant processedAt;
}
