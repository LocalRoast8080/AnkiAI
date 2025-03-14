package com.example.ankiai.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnkiNoteCardFull {
    @JsonProperty("noteId")
    private long noteId;
    @JsonProperty("fields")
    private AnkiNoteFullFields fields;
}