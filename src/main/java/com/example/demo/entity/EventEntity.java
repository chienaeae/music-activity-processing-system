package com.example.demo.entity;

import java.time.Instant;

import com.example.demo.model.ActionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType action;
    
    @Column(name = "song_id")
    private String songId;
    
    @Column(name = "timestamp")
    private Instant timestamp;
    
    @Column(name = "source")
    private String source;
    
    @Column(name = "processed_at")
    private Instant processedAt;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "count", nullable = false)
    private Integer count = 0;
    
    // V2 migration fields
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "os_version")
    private String osVersion;
    
    @Column(name = "app_version")
    private String appVersion;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "location")
    private String location;
} 