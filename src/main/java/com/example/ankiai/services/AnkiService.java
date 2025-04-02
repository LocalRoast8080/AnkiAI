package com.example.ankiai.services;

import com.example.ankiai.exceptions.RestException;
import com.example.ankiai.exceptions.SearchException;
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
import org.springframework.web.client.RestClientException;

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

    public List<AnkiDeck> getDecks() {
        AnkiAction action = new AnkiAction("deckNames");

        String res;
        try {
            res = ankiClient.post()
                    .uri("/")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to perform get decks REST call.", e.getMessage());
        }

        try {
            AnkiResponse<List<AnkiDeck>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiDeck>>>() {
            });
            return response.result;
        } catch (JsonProcessingException e) {
            // this means a malformed response from the client need to log the input to the llm and the output.
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public AnkiNoteCard getNoteCard(long noteId) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", new long[]{noteId});
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Fail to perform get note card REST call.", e.getMessage());
        }

        try {
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {
            });
            var note = ankiMapper.mapToAnkiNoteCards(response.result);
            return note.getFirst();
            // this means a malformed response from the client need to log the input to the llm and the output.
        } catch (JsonProcessingException e) {
            // this means a malformed response from the client need to log the input to the llm and the output.
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<AnkiNoteCard> getNoteCards(List<Long> noteCardIds) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", noteCardIds);
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to perform get note cards REST call.", e.getMessage());
        }

        try {
            AnkiResponse<List<AnkiNoteCardFull>> response = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {
            });
            if (response.result.size() == 1 && response.result.getFirst().getNoteId() == 0) {
                log.error("Failed to get NoteCard: {}", response.error);
                return Collections.emptyList();
            }
            return ankiMapper.mapToAnkiNoteCards(response.result);

        } catch (JsonProcessingException e) {
            // this means a malformed response from the client need to log the input to the llm and the output.
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Long> getAllNoteCardIds(String deckName) {
        AnkiAction action = new AnkiAction("findNotes");

        Map<String, Object> params = new HashMap<>();
        params.put("query", "deck:" + deckName);
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to perform get all note ids REST call.", e.getMessage());
        }

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

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to get note card ids REST call.", e.getMessage());
        }

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

    public AnkiNoteCard updateNote(AnkiNoteCard noteCard) {
        AnkiNoteUpdateReq updateModel = ankiMapper.mapToAnkiUpdateReq(noteCard);
        var action = new AnkiAction("updateNote");

        Map<String, Object> params = new HashMap<>();
        params.put("note", updateModel);
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to perform update note REST call.", e.getMessage());
        }

        try {
            AnkiResponse<String> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<String>>() {
            });

            if (ankiRes.getError() != null) {
                log.error("Failed to update NoteCard: {}", ankiRes.getError());
                return null;
            }
            // AnkiConnect Returns Null on 200. Returning model to simulate success.
            return noteCard;

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<Long> createNoteCards(List<AnkiNoteCard> noteCards, String deckName) {
        List<AnkiCreateRequest> createRequest = ankiMapper.mapToAnkiCreateReqs(noteCards, deckName);
        var action = new AnkiAction("addNotes");

        Map<String, Object> params = new HashMap<>();
        params.put("notes", createRequest);
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed to perform create note cards REST call.", e.getMessage());
        }

        try {
            AnkiResponse<List<Long>> ankiRes = mapper.readValue(res, new TypeReference<AnkiResponse<List<Long>>>() {
            });
//            {
//                "result" : null,
//                    "error" : "['cannot create note because it is a duplicate']"
//            }
            if (ankiRes.getError() != null) {
                log.error("Failed to create NoteCard: {}", ankiRes.getError());
                return Collections.emptyList();
            }
            // AnkiConnect Returns Null on 200. Returning model to simulate success.
            return ankiRes.getResult();

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<AnkiNoteCard> searchNotes(String deckName, String query) {
        AnkiAction action = new AnkiAction("notesInfo");

        Map<String, Object> params = new HashMap<>();
        params.put("query", String.format("deck:%s %s", deckName, query));
        action.setParams(params);

        String res;
        try {
            res = ankiClient.post()
                    .uri("")
                    .body(action.toJsonString())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RestException("Failed perform to search notes REST call.", e.getMessage());
        }

        try {
            AnkiResponse<List<AnkiNoteCardFull>> notesResponse = mapper.readValue(res, new TypeReference<AnkiResponse<List<AnkiNoteCardFull>>>() {
            });

            if (notesResponse.getError() != null) {
                log.error("Failed to find notes: {}", notesResponse.getError());
                throw new SearchException("Failed to find notes");
            }

            if (notesResponse.getResult().isEmpty()) {
                return Collections.emptyList();
            }

            return ankiMapper.mapToAnkiNoteCards(notesResponse.result);

        } catch (JsonProcessingException e) {
            log.error("Failed to process JSON: {}", e.getMessage(), e);
            throw new SearchException("Failed to process search results");
        }
    }
}