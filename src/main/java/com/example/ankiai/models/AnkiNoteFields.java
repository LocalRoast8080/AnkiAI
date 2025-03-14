package com.example.ankiai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnkiNoteFields {
    @JsonProperty("Front")
    public String front;
    @JsonProperty("Back")
    public String back;
}