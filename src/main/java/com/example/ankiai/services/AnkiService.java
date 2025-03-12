package com.example.ankiai.services;

import com.example.ankiai.mappers.AnkiNoteCardMapper;
import com.example.ankiai.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AnkiService {

    private final RestClient ankiClient;
    private final ObjectMapper mapper;
    private final AnkiNoteCardMapper ankiMapper;

    public AnkiService(@Qualifier("ankiConnectClient") RestClient ankiClient, ObjectMapper mapper, AnkiNoteCardMapper ankiMapper) {
        this.ankiClient = ankiClient;
        this.mapper = mapper;
        this.ankiMapper = ankiMapper;
    }

    // add anki response that returns  result T or String Error
    public List<AnkiDeck> getDecks() {
        AnkiAction action = new AnkiAction("deckNames");

        var res = ankiClient.post()
                .uri("/")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<List<AnkiDeck>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiDeck>>>() {
            });
            return response.result;

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public AnkiNoteCard getNoteCard(long noteId) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", new long[]{noteId});
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {
            });
            var note = ankiMapper.mapToAnkiNoteCards(response.result);
            return note.getFirst();

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<AnkiNoteCard> getNoteCards(List<Long> noteCardIds) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", noteCardIds);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {
            });
            return ankiMapper.mapToAnkiNoteCards(response.result);

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> getAllNoteCardIds(String deckName) {
        AnkiAction action = new AnkiAction("findNotes");

        Map<String, Object> params = new HashMap<>();
        params.put("query", "deck:" + deckName);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<List<Long>> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<List<Long>>>() {
            });

            if (ankiRes.getError() != null) {
                log.error("Failed to update NoteCard: {}", ankiRes.getError());
                return Collections.emptyList();
            }

            return ankiRes.getResult();

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> getNoteCardIds(String deckName, int limit) {
        AnkiAction action = new AnkiAction("findNotes");

        Map<String, Object> params = new HashMap<>();
        params.put("query", "deck:" + deckName);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<List<Long>> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<List<Long>>>() {
            });

            if (ankiRes.getError() != null) {
                log.error("Failed to update NoteCard: {}", ankiRes.getError());
                return Collections.emptyList();
            }

            var limitedIds = ankiRes.getResult();

            return ankiRes.getResult().subList(0, Math.min(limitedIds.size(), limit));
        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    // https://git.sr.ht/~foosoft/anki-connect#codeupdatenotefieldscode Docs, add to interface later
    public AnkiNoteCard updateNote(AnkiNoteCard noteCard) {
        AnkiNoteUpdateReq updateModel = ankiMapper.mapToAnkiNoteReq(noteCard);

        var action = new AnkiAction("updateNoteFields");
        Map<String, Object> params = new HashMap<>();

        params.put("note", updateModel);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try {
            AnkiResponse<String> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<String>>() {
            });

            if (ankiRes.getError() != null) {
                log.error("Failed to update NoteCard: {}", ankiRes.getError());
                return null;
            }

            // AnkiConnect Returns Null on 200. Returning model to simulate success. This will be wrapped at a later time.
            return noteCard;

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public AnkiNoteCard createNoteCard(AnkiNoteCard noteCard) {
        throw new UnsupportedOperationException("This method is not implemented yet");
    }
}