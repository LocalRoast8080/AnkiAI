package com.example.ankiai.services;

import com.example.ankiai.models.AnkiNoteCard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * updates cards
 */
@Service
public class NoteCardProcessorService {

    private final XAiService xAiService;
    private final AnkiService ankiService;
    private final ObjectMapper objectMapper;

    public NoteCardProcessorService(XAiService xAiService, AnkiService ankiService, ObjectMapper objectMapper) {
        this.xAiService = xAiService;
        this.ankiService = ankiService;
        this.objectMapper = objectMapper;
    }

    public List<AnkiNoteCard> getNoteCards(String deckName, int noteCardLimit){
        var allNotesIds = ankiService.getAllNoteCardIds(deckName);

        // TODO add error to response wrapper. Null here could imply invalid deck name or it doesnt not exists
        if(  allNotesIds == null ||allNotesIds.isEmpty()){return null;}

        // TODO should i let the user add a number too large and auto correct?
        var noteIds = allNotesIds.subList(0, Math.min(noteCardLimit, allNotesIds.size()));

        return ankiService.getNoteCards(noteIds);
    }

    public List<AnkiNoteCard> spellCheck(List<AnkiNoteCard> noteCards) {

        var aiRes = xAiService.spellCheckNotes(noteCards);

        // TODO can I get a null exception if .content is not there? find out in testing
        var resString = aiRes.content.getFirst().message.content;
        if (resString.isBlank()) {
            // TODO will probably need a wrapper response to put errors into and the action result for returning in the controller
            // here the error should be added
            System.err.println("Failed to retrieve information");
            return null;
        }

        List<AnkiNoteCard> mappedNotes = new ArrayList<>();
        try {
            mappedNotes = objectMapper.readValue(resString, new TypeReference<List<AnkiNoteCard>>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return mappedNotes;
    }

    public List<AnkiNoteCard> expandAnswers(List<AnkiNoteCard> noteCards) {
        var aiRes = xAiService.expandNotes(noteCards);

        var resString = aiRes.content.getFirst().message.content;
        if (resString.isBlank()) {
            // TODO will probably need a wrapper response to put errors into and the action result for returning in the controller
            // here the error should be added
            System.err.println("Failed to retrieve information");
            return null;
        }

        List<AnkiNoteCard> mappedNotes = new ArrayList<>();
        try {
            mappedNotes = objectMapper.readValue(resString, new TypeReference<List<AnkiNoteCard>>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return mappedNotes;
    }
}
