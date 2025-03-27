package com.example.ankiai.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    @NotBlank(message = "deckName is required")
    private String deckName;
    
    @NotBlank(message = "query is required")
    private String query;
} 