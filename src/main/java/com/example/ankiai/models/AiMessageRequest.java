package com.example.ankiai.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiMessageRequest {

    List<AiPromptMessage> messages;
    String model;
    boolean stream;
    double temperature;


    public AiMessageRequest(String systemPrompt, String userPrompt) {

        var systemMessage = new AiPromptMessage("system", systemPrompt);
        var userMessage = new AiPromptMessage("user", userPrompt);
        this.messages = List.of(systemMessage,userMessage);
        this.model = "grok-2-latest";
        this.stream = false;
        this.temperature = 1.0;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var res = objectMapper.writeValueAsString(this);
            return res;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting AnkiAction to JSON", e);
        }
    }
}
