package com.example.ankiai.services;

import com.example.ankiai.models.AnkiNoteCard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NoteCardProcessorService {

    private final XAiService xAiService;
    private final ObjectMapper objectMapper;

    public NoteCardProcessorService(XAiService xAiService, ObjectMapper objectMapper) {
        this.xAiService = xAiService;
        this.objectMapper = objectMapper;
    }

    public List<AnkiNoteCard> spellCheck(List<AnkiNoteCard> noteCards) {

        var aiRes = xAiService.spellCheckNotes(noteCards);
        if (aiRes == null) {
            log.error("Failed to spell check note cards");
            return Collections.emptyList();
        }

        var resString = aiRes.content.getFirst().message.content;
        if (resString.isBlank()) {
            log.error("Failed to spell check note cards: {}", aiRes.toString());
            return Collections.emptyList();
        }

        List<AnkiNoteCard> mappedNotes;
        try {
            mappedNotes = objectMapper.readValue(resString, new TypeReference<List<AnkiNoteCard>>() {
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }

        return mappedNotes;
    }

    public List<AnkiNoteCard> expandAnswers(List<AnkiNoteCard> noteCards) {
        var aiRes = xAiService.expandNotes(noteCards);
        if (aiRes == null) {
            log.error("Failed to expand note cards, null response:");
            return Collections.emptyList();
        }

        var resString = aiRes.content.getFirst().message.content;
        if (resString.isBlank()) {
            log.error("Failed to expand note cards, blank response: {}", aiRes);
            return Collections.emptyList();
        }

        List<AnkiNoteCard> mappedNotes;
        try {
            mappedNotes = objectMapper.readValue(resString, new TypeReference<List<AnkiNoteCard>>() {
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to process map json response: {}", e.getMessage(), e);
            return Collections.emptyList();
        }

        return mappedNotes;
    }

    public List<AnkiNoteCard> generateCards(String text) {
        var aiRes = xAiService.generateCards(text);
        if(aiRes == null){
            log.error("Failed to generate cards");
            return Collections.emptyList();
        }

        var resString = aiRes.content.getFirst().message.content;
        if (resString.isBlank()) {
            log.error("Failed to generate cards, blank response: {}", aiRes);
            return Collections.emptyList();
        }

        List<AnkiNoteCard> mappedNotes;
        try {
            mappedNotes = objectMapper.readValue(resString, new TypeReference<List<AnkiNoteCard>>() {
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to process map json response: {}", e.getMessage(), e);
            return Collections.emptyList();
        }

        return mappedNotes;
    }
}
