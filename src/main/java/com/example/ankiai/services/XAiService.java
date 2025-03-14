package com.example.ankiai.services;

import com.example.ankiai.models.AiMessageRequest;
import com.example.ankiai.models.AiResponseMessage;
import com.example.ankiai.models.AnkiNoteCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class XAiService {

    private final RestClient grokClient;

    // Should this be here? 3/6 I think not. If I ever want to change AI provider these are stuck here.
    private final String spellCheckPrompt = "perform spelling updated / fixes and grammatical corrections on the 'front' and 'back' fields" +
                                            "Do not update the answer, only fix spelling and complete words if needed.";
//    private final String expandAnswerPrompt = "If a card's 'back' is sparse or lacks detail, expand it with relevant, accurate information while maintaining clarity." +
//                                              "If the 'back' warrants a one word response do not add more information" +
//                                              "If the 'back' is already detailed, do not add more information." +
//                                              "If the back can be formatted as a list, do so and do not add more information.";
    private final String expandAnswerPrompt = "If a card's 'back' is sparse, incorrect, or lacks detail, replace it with accurate information. " +
                                              "For questions expecting a single-word answer (e.g., 'What is the capital of Canada?'), provide only the word (e.g., 'Ottawa'). " +
                                              "For questions requiring a definition (e.g., 'What is photosynthesis?', 'What is gravity?'), provide a detailed, scientifically precise sentence (e.g., 'Photosynthesis is the biochemical process by which green plants, algae, and some bacteria convert light energy into chemical energy, using carbon dioxide and water to produce glucose and oxygen,' or 'Gravity is a fundamental force of nature, the force of attraction between objects with mass, responsible for keeping us on the ground and planets in orbit'). " +
                                              "For questions asking for multiple items (e.g., 'What are the 5 Solid Principles?'), provide a concise list without explanations (e.g., '- Single Responsibility Principle\n- Open/Closed Principle\n- Liskov Substitution Principle\n- Interface Segregation Principle\n- Dependency Inversion Principle'). " +
                                              "If the 'back' is already detailed and correct, do not modify it.";

    public XAiService(@Qualifier("grokClient") RestClient grokClient) {
        this.grokClient = grokClient;
    }

    private String createPrompt(String prompt) {
        return "You are an Anki Card processor. For each card in the input JSON array, " + prompt +
               "Return the response as ONLY a JSON array matching the input format, with updated 'front' , 'back' fields and the original 'noteId' field" +
               "Do not add or modify any other fields (e.g., 'noteId'), and ensure the output JSON structure is identical to the input." +
               "Response Example : [{'noteId': long, 'front': 'card question', 'back': 'card answer' }]";
    }

    public AiResponseMessage expandNotes(List<AnkiNoteCard> noteCards) {

        var prompt = createPrompt(expandAnswerPrompt);
        var req = new AiMessageRequest(prompt, noteCards.toString());

        try{
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);
        }catch (Exception e){
            log.error("Failed Expand Notes {}:", e);
            return null;
        }
    }

    public AiResponseMessage spellCheckNotes(List<AnkiNoteCard> noteCards) {

        var prompt = createPrompt(spellCheckPrompt);
        var req = new AiMessageRequest(prompt, noteCards.toString());

        try{
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);

        }catch (Exception e){
            log.error("Failed Spell Check {}:", e);
            return null;
        }

    }
}
