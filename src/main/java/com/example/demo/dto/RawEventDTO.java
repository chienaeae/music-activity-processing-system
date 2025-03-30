package com.example.demo.dto;

import java.time.Instant;

import com.example.demo.model.ActionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawEventDTO {
    private String userId;
    private String action;
    private String songId;
    private Instant timestamp;
    
    /**
     * Get the converted ActionType enum value
     * @return The corresponding ActionType enum value, or null if conversion fails
     */
    public ActionType getActionType() {
        return ActionType.fromString(action);
    }
} 
