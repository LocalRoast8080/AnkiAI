package com.example.ankiai.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiPromptMessage {
    public String role;
    public String content;

    public AiPromptMessage(String role, String content){
        this.role = role;
        this.content = content;
    }
}