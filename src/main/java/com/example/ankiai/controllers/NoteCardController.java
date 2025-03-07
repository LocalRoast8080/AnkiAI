package com.example.ankiai.controllers;

import com.example.ankiai.models.AnkiDeck;
import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.services.AnkiService;
import com.example.ankiai.services.NoteCardProcessorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class NoteCardController {

    private final NoteCardProcessorService noteCardProcessorService;
    private final AnkiService ankiService;

    public NoteCardController(NoteCardProcessorService ncpService, AnkiService ankiService){
        this.noteCardProcessorService = ncpService;
        this.ankiService = ankiService;
    }

    @GetMapping("/deckNames")
    public List<AnkiDeck> getDeckNames(){
        return ankiService.getDecks();
    }

    @GetMapping("/noteCards")
    public List<AnkiNoteCard> getNoteCards(@RequestParam("deckName") String deckName, @RequestParam("noteCardLimit") int noteCardLimit){
        var decks = ankiService.getDecks();
        var deck = new AnkiDeck(deckName);
        if(!decks.contains(deck)){return null;}

        return noteCardProcessorService.getNoteCards(deckName, noteCardLimit);
    }

    @PatchMapping("/updateNoteCard")
    public List<AnkiNoteCard> patchNoteCards(@RequestBody List<AnkiNoteCard> noteCards){
        throw new UnsupportedOperationException();
    }
}
