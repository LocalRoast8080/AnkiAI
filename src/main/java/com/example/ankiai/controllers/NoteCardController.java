package com.example.ankiai.controllers;

import com.example.ankiai.models.AnkiDeck;
import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.models.SearchRequest;
import com.example.ankiai.services.AnkiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/note")
public class NoteCardController {

    private final AnkiService ankiService;

    @GetMapping("/deckNames")
    public ResponseEntity<List<AnkiDeck>> getDeckNames() {
        var res = ankiService.getDecks();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/noteIds")
    public ResponseEntity<List<Long>> getNoteCardIds(@RequestParam("deckName") String deckName, @RequestParam("noteIdLimit") int noteIdLimit) {
        var res = ankiService.getNoteCardIds(deckName, noteIdLimit);
        if (res.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/noteCards")
    public ResponseEntity<List<AnkiNoteCard>> getNoteCards(@RequestBody List<Long> noteCardIds) {
        if (noteCardIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var res = ankiService.getNoteCards(noteCardIds);
        if (res.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(res);
    }

    @PatchMapping("/updateNoteCard")
    public ResponseEntity<AnkiNoteCard> updateNoteCard(@RequestBody AnkiNoteCard noteCard) {
        var res = ankiService.updateNote(noteCard);
        if (res == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/createNoteCard")
    public ResponseEntity<List<Long>> createNoteCard(@RequestBody List<AnkiNoteCard> noteCard, @RequestParam("deckName") String deckName) {
        var res = ankiService.createNoteCards(noteCard, deckName);
        if(res == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/search")
    public ResponseEntity<List<AnkiNoteCard>> searchNoteCards(@Valid @RequestBody SearchRequest request) {
        var notes = ankiService.searchNotes(request.getDeckName(), request.getQuery());
        return ResponseEntity.ok(notes);
    }
}
