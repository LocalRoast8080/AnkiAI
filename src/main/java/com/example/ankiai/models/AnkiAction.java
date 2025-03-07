package com.example.ankiai.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnkiAction {
    private String action;
    private int version = 6;
    private Map<String, Object> params = Map.of();

    // The Lombok constructor was not registering
    public AnkiAction(String action){
        this.action = action;
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
