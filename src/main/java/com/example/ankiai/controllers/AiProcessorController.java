package com.example.ankiai.controllers;

import com.example.ankiai.execptions.EmptyFileException;
import com.example.ankiai.execptions.InvalidFileException;
import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.services.NoteCardProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    /**
     * Generates Anki note cards from raw text input.
     * @param text the raw text containing lines formatted with '?' or '??' to define card questions and context
     * @return a ResponseEntity containing a list of AnkiNoteCard objects with 'front' and 'back' fields populated
     */
    @PostMapping("/generateCards")
    public ResponseEntity<List<AnkiNoteCard>> generateCards(@RequestBody String text){
        // validate input
        var res = noteCardProcessorService.generateCards(text);

        if(res.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/generateCards/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AnkiNoteCard>> generateCardsUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new EmptyFileException("File uploaded is empty.");
        }

        var fileName = file.getOriginalFilename();
        if(fileName == null || !fileName.toLowerCase().endsWith(".txt")){
            throw new InvalidFileException("Only .txt files can be uploaded");
        }

        // validate input
        var stringFile = new String(file.getBytes());
        var res = noteCardProcessorService.generateCards(stringFile);

        if(res.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(res);
    }
}
