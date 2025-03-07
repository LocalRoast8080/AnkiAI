package com.example.ankiai.interfaces;

import com.example.ankiai.models.AnkiDeck;
import com.example.ankiai.models.AnkiNoteCard;

import java.util.List;

/**
 * Defines the interface for controlling Anki note card operations via REST endpoints.
 */
public interface INoteCardController {

    /**
     * Retrieves a list of all available Anki deck names.
     *
     * @return a list of {@link AnkiDeck} objects representing deck names
     */
    public List<AnkiDeck> getDeckNames();

    /**
     * Retrieves a limited number of note card IDs for a specified deck.
     *
     * @param deckName    the name of the deck to query
     * @param noteIdLimit the maximum number of note card IDs to return
     * @return a list of {@link Long} representing the note card IDs
     */
    public List<Long> getNoteIds(String deckName, int noteIdLimit);

    /**
     * Retrieves a limited number of note cards for a specified deck.
     *
     * @param deckName    the name of the deck to query
     * @param noteIdLimit the maximum number of note cards to return
     * @return a list of {@link AnkiNoteCard} objects matching the criteria
     */
    public List<AnkiNoteCard> getNoteCards(String deckName, int noteIdLimit);

    /**
     * Updates an existing Anki note card with new data.
     *
     * @param noteCard the {@link AnkiNoteCard} object containing updated information
     * @return the updated {@link AnkiNoteCard} object
     */
    public AnkiNoteCard updateNoteCard(AnkiNoteCard noteCard);
}
