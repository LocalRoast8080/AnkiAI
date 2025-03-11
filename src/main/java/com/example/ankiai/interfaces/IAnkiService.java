package com.example.ankiai.interfaces;

import com.example.ankiai.models.AnkiDeck;
import com.example.ankiai.models.AnkiNoteCard;

import java.util.List;

/**
 * Provides an interface for interacting with Anki-Connect to manage and retrieve Anki objects.
 */
public interface IAnkiService {

    /**
     * Retrieves a list of all available Anki decks.
     *
     * @return a list of {@link AnkiDeck} objects representing all decks
     */
    public List<AnkiDeck> getDecks();

    /**
     * Retrieves a single Anki note card by its unique identifier.
     *
     * @param noteId the ID of the note card to retrieve
     * @return an {@link AnkiNoteCard} object corresponding to the given ID
     */
    public AnkiNoteCard getNoteCard(long noteId);

    /**
     * Retrieves a list of Anki note cards based on a list of note card IDs.
     *
     * @param noteCardIds the list of note card IDs to retrieve
     * @return a list of {@link AnkiNoteCard} objects matching the provided IDs
     */
    public List<AnkiNoteCard> getNoteCards(List<Long> noteCardIds);

    /**
     * Retrieves all note card IDs for a specified deck.
     *
     * @param deckName the name of the deck to query
     * @return a list of {@link Long} representing all note card IDs in the deck
     */
    public List<Long> getAllNoteCardIds(String deckName);

    /**
     * Retrieves a limited number of note card IDs for a specified deck.
     *
     * @param deckName the name of the deck to query
     * @param limit    the maximum number of note card IDs to return
     * @return a list of {@link Long} representing the note card IDs up to the limit
     */
    public List<Long> getNoteCardIds(String deckName, int limit);

    /**
     * Updates an existing Anki note card with new data.
     *
     * @param noteCard the {@link AnkiNoteCard} object containing updated information
     * @return the updated {@link AnkiNoteCard} object
     */
    public AnkiNoteCard updateNote(AnkiNoteCard noteCard);

    /**
     * Creates a new Anki note card in the specified deck.
     *
     * @param noteCard the {@link AnkiNoteCard} object to create
     * @return the created {@link AnkiNoteCard} object
     */
    public AnkiNoteCard createNoteCard(AnkiNoteCard noteCard);
}
