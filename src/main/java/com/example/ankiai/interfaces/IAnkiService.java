package com.example.ankiai.interfaces;

import com.example.ankiai.models.AnkiDeck;

import java.util.List;

/**
 * Provides an interface for interacting with Anki-Connect to manage and retrieve Anki objects.
 */
public interface IAnkiService {

    /**
     * Retrieves a list of all available decks in Anki.
     *
     * @return a list of {@link AnkiDeck} objects representing Anki decks.
     * todo - add throws tab. look at anki connect docs
     */
    public List<String> getDecks();

    public void getNote(int noteId);


    /**
     * A generic test method to be removed later;
     */
    public void test();
}
