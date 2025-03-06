package com.example.ankiai.services;

import com.example.ankiai.configuration.XAiConfigProperties;
import com.example.ankiai.mappers.AnkiNoteCardMapper;
import com.example.ankiai.models.AnkiAction;
import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.models.AnkiNoteCardFull;
import com.example.ankiai.models.AnkiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.el.MethodNotFoundException;
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

    public String getDecks() {
        AnkiAction action = new AnkiAction("deckNames");

        return ankiClient.post()
                .uri("/")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);
    }

    public AnkiNoteCard getNoteInfo(long noteId) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", new long[]{noteId});
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try{
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {} );
            var note = ankiMapper.mapToAnkiNoteCards(response.result);
            return note.getFirst();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AnkiNoteCard> getNoteInfo(List<Long> noteIds) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", noteIds);
        action.setParams(params);

        var res = ankiClient.post()
                .uri("")
                .body(action.toJsonString())
                .retrieve()
                .body(String.class);

        try{
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {} );
            var notes = ankiMapper.mapToAnkiNoteCards(response.result);
            return notes;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Long> getAllNotesIds(String deckName) {
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
            AnkiResponse<List<Long>> ankiRes = mapper.readValue(
                    res,
                    new TypeReference<AnkiResponse<List<Long>>>() {
                    }
            );

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
    public String updateNote(AnkiNoteCardFull note){
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

    public String createNoteCard(){
        throw new UnsupportedOperationException("This method is not implemented yet");
    }
}