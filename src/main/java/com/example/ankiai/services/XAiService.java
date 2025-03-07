package com.example.ankiai.services;

import com.example.ankiai.models.AiMessageRequest;
import com.example.ankiai.models.AiResponseMessage;
import com.example.ankiai.models.AnkiNoteCard;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class XAiService {

    private final RestClient grokClient;

    // Should this be here? 3/6 I think not. If I ever want to change AI provider these are stuck here.
    private final String spellCheckPrompt = "perform spell-checking and grammatical corrections on the 'front' and 'back' fields";
    private final String expandAnswerPrompt = "If a card's 'back' is sparse or lacks detail, expand it with relevant, accurate information while maintaining clarity." +
                                              "If the 'back' warrants a one word response do not add more information";

    public XAiService(@Qualifier("grokClient") RestClient grokClient) {
        this.grokClient = grokClient;
    }

    private String createPrompt(String prompt) {
        return "You are an Anki Card processor. For each card in the input JSON array, " + prompt +
               "Return the response as ONLY a JSON array matching the input format, with updated 'front' , 'back' fields and the original 'noteId' field" +
               "Do not add or modify any other fields (e.g., 'noteId'), and ensure the output JSON structure is identical to the input." +
               "Response Example : [{'noteId': long, 'front': 'card question', 'back': 'card answer' }]";
    }

    public AiResponseMessage expandNotes(List<AnkiNoteCard> notes) {

        var prompt = createPrompt(expandAnswerPrompt);
        var req = new AiMessageRequest(prompt, notes.toString());

        // Convert the string to note cards or Ai response
        try {
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);
        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            throw e;
        }
    }

    public AiResponseMessage spellCheckNotes(List<AnkiNoteCard> notes) {

        var prompt = createPrompt(spellCheckPrompt);
        var req = new AiMessageRequest(prompt, notes.toString());

        // Convert the string to note cards or Ai response
        try {
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);
        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            throw e;
        }
    }
}
