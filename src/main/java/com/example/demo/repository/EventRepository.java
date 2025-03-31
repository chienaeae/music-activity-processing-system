package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.EventEntity;
import com.example.demo.model.ActionType;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    
    List<EventEntity> findByUserId(String userId);
    
    List<EventEntity> findByAction(ActionType action);
    
    List<EventEntity> findByCategory(String category);
    
    List<EventEntity> findByCategoryAndAction(String category, ActionType action);
    
    List<EventEntity> findByUserIdAndAction(String userId, ActionType action);
} 