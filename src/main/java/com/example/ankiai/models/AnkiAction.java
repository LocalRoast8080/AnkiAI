package com.example.ankiai.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;


public class AnkiAction {
    private String action;
    // Optional version field if needed by Anki
    private int version = 6;
    // Optional params field
    private Map<String, Object> params = Map.of();

    // Constructor
    public AnkiAction(String action) {
        this.action = action;
    }

    // Getters and setters (important for serialization)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting AnkiAction to JSON", e);
        }
    }
}
