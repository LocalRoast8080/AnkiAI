package com.example.ankiai.controllers;

import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.services.NoteCardProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/aiProcessor")
public class AiProcessorController {

    private final NoteCardProcessorService noteCardProcessorService;

    // verify validation on input
    @PostMapping("/spellCheck")
    public ResponseEntity<List<AnkiNoteCard>> spellCheckCards(@RequestBody List<AnkiNoteCard> noteCards){
        if(noteCards.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        var res = noteCardProcessorService.spellCheck(noteCards);

        if(res.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/expandAnswers")
    public ResponseEntity<List<AnkiNoteCard>> expandAnswers(@RequestBody List<AnkiNoteCard> noteCards){
        if(noteCards.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        var res = noteCardProcessorService.expandAnswers(noteCards);

        if(res.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(res);
    }
    // create more cards
}
