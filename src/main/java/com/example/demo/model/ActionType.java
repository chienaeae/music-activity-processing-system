package com.example.demo.model;

/**
 * Represents the type of action that a user can perform
 */
public enum ActionType {
    PLAY,
    PAUSE,
    SKIP,
    LIKE,
    DISLIKE,
    SHARE,
    LOGIN,
    LOGOUT;
    
    /**
     * Convert string to enum value, case insensitive
     * 
     * @param action action string
     * @return corresponding enum value, or null if no match
     */
    public static ActionType fromString(String action) {
        if (action == null) {
            return null;
        }
        
        try {
            return ActionType.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
} 