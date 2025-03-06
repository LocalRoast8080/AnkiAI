package com.example.ankiai.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnkiNoteCard {
    public long noteId;
    public String front;
    public String back;
}
