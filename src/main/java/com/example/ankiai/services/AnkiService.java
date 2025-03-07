package com.example.ankiai.services;

import com.example.ankiai.configuration.XAiConfigProperties;
import com.example.ankiai.mappers.AnkiNoteCardMapper;
import com.example.ankiai.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class AnkiService {

    private final RestClient ankiClient;
    private final XAiConfigProperties xAiConfigProperties;
    private final ObjectMapper mapper;
    private final AnkiNoteCardMapper ankiMapper;

    public AnkiService(@Qualifier("ankiConnectClient") RestClient ankiClient, XAiConfigProperties xAiConfigProperties, ObjectMapper mapper, AnkiNoteCardMapper ankiMapper) {
        this.ankiClient = ankiClient;
        this.xAiConfigProperties = xAiConfigProperties;
        this.mapper = mapper;
        this.ankiMapper = ankiMapper;
    }

    // still need to add error code handeling
    // create resopnse wrapper class
    // probs make a method to wrap action builder

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
            throw new RuntimeException(e);
        }
    }

    public AnkiNoteCard getNoteCards(long noteId) {
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
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {});
            var note = ankiMapper.mapToAnkiNoteCards(response.result);
            return note.getFirst();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {});
            var notes = ankiMapper.mapToAnkiNoteCards(response.result);
            return notes;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            AnkiResponse<List<Long>> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<List<Long>>>() {});

            if (ankiRes.getError() != null) {
                throw new RuntimeException(ankiRes.getError());
            }

            return ankiRes.getResult();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    how should updaint a card work?
    tags can be assined if they do not exist
    no ignore tags for now
     */
    public String updateNote(AnkiNoteCardFull note) {
        var action = new AnkiAction("updateNoteFields");

        Map<String, Object> params = new HashMap<>();
        params.put("note", note);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        return res;
    }

    public String createNoteCard() {
        throw new UnsupportedOperationException("This method is not implemented yet");
    }
}