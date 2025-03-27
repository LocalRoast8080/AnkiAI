package com.example.ankiai.services;

import com.example.ankiai.execptions.AiException;
import com.example.ankiai.models.AiMessageRequest;
import com.example.ankiai.models.AiResponseMessage;
import com.example.ankiai.models.AnkiNoteCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class XAiService {

    private final RestClient grokClient;

    private final String formattingInstruction = "For questions expecting a single-word answer (e.g., 'What is the capital of Canada?'), provide only the word (e.g., 'Ottawa'). " +
                                                 "For questions requiring a definition (e.g., 'What is photosynthesis?', 'What is gravity?'), provide a detailed, scientifically precise answer " +
                                                 "(e.g., 'Photosynthesis is the biochemical process by which green plants, algae, and some bacteria convert light energy into chemical energy, " +
                                                 "using carbon dioxide and water to produce glucose and oxygen,' or 'Gravity is a fundamental force of nature, the force of attraction between objects with mass, " +
                                                 "responsible for keeping us on the ground and planets in orbit'). " +
                                                 "For questions asking for multiple items (e.g., 'What are the 5 Solid Principles?'), provide a concise list without explanations " +
                                                 "(e.g., '- Single Responsibility Principle\n- Open/Closed Principle\n- Liskov Substitution Principle\n- Interface Segregation Principle\n- Dependency Inversion Principle'). " +
                                                 "Answers should not be more than 50 words typically." +
                                                 "For complex subject like coding and science you can go up to 50 word. IF needed past";

    private final String spellCheckPrompt = "perform spelling updated / fixes and grammatical corrections on the 'front' and 'back' fields" +
                                            "Do not update the answer, only fix spelling and complete words if needed.";

    private final String expandAnswerPrompt = "If a card's 'back' is sparse, incorrect, or lacks detail, replace it with accurate information." +
                                              formattingInstruction +
                                              "If the 'back' is already detailed and correct, do not modify it.";

    private final String generateCards = """
           You are an Anki Card processor and professor of all things. Parse the raw text input and generate note cards based on the following legend:
           
           - '?' = Identify the primary term in the line (e.g., 'rain' in '? rain - does it fall often?' or 'HttpFactory' in '? HttpFactory factory = new HttpFactory()'),
           create a single question in the form 'What is [term]?', and use any text after a dash ('-') as context for a detailed, accurate answer.
           If no dash is present, focus only on the first significant term (e.g., a class name, noun, or annotated word) and ignore the rest unless it’s indented context.
           Do not treat code snippets as full questions.
           
           - '??' = Treat the line as a subject, generate a context-appropriate question based on the input
                    (e.g., '?? HashMap' might become 'What is a HashMap?', or '?? x = 5;' might become 'What does this code do x = 5?'), and provide a detailed, accurate answer.
                    If the information after '-' the context line, can be turned into two cards do so.
                    
           - '?EX' = apply the rules from '?' then provide more detailed answer making sure to explore the complexity of the input.
            (e.g., '?EX RestClient - what methods does it have? method chaining? general information? restClient.post()
                             .uri("https://api.example.com/users")
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new User("John", "Doe"))
                             .retrieve()
                             .body(String.class);
             The response here should dive into the .retreive() .body() and methods not listed but should be known.                
             
           
           - Lines without '?+', '?:', or '??' are ignored unless indented under a question, in which case they provide context for the answer.
           
           Understanding Context:
           - ? RestClient - what methods does it have? method chaining? general information? : on this line 'general information' implies making a card like 'What is RestClient?'
           
           If needed split a line into multiple cards. One card should always be one question.
           
           Apply the following rules to all answers: %s
           Fix spelling and grammatical errors in the 'front' field without changing its intent.
           
           Return the response as ONLY a JSON array with 'front' and 'back' fields.
           Response Example: [{'front': 'card question', 'back': 'card answer'}]""".formatted(formattingInstruction);

    public XAiService(@Qualifier("grokClient") RestClient grokClient) {
        this.grokClient = grokClient;
    }

    /**
     * Creates a generic prompt intended to work on AnkiNoteCards.
     * @param prompt the specific instruction to process the Anki cards (e.g., spell check, expand answers)
     * @return a formatted prompt string combining the base Anki processor instruction with the provided prompt,
     *         specifying JSON output with 'noteId', 'front', and 'back' fields
     */
    private String createAnkiCardPrompt(String prompt) {
        return "You are an Anki Card processor. For each card in the input JSON array, " + prompt +
               "Return the response as ONLY a JSON array matching the input format, with updated 'front' , 'back' fields and the original 'noteId' field" +
               "Do not add or modify any other fields (e.g., 'noteId'), and ensure the output JSON structure is identical to the input." +
               "Response Example : [{'noteId': long, 'front': 'card question', 'back': 'card answer' }]";
    }

    public AiResponseMessage expandNotes(List<AnkiNoteCard> noteCards) {

        var prompt = createAnkiCardPrompt(expandAnswerPrompt);
        var req = new AiMessageRequest(prompt, noteCards.toString());

        try {
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);
        } catch (Exception e) {
            log.error("Failed Expand Notes {}:", e);
            return null;
        }
    }

    public AiResponseMessage spellCheckNotes(List<AnkiNoteCard> noteCards) {

        var prompt = createAnkiCardPrompt(spellCheckPrompt);
        var req = new AiMessageRequest(prompt, noteCards.toString());

        try {
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);

        } catch (Exception e) {
            throw new AiException("Failed spell checking try again");
        }
    }

    public AiResponseMessage generateCards(String text){
        // validate this is a correct input type.

        var req = new AiMessageRequest(generateCards, text);

        try {
            return grokClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(AiResponseMessage.class);

        } catch (Exception e) {
            throw new AiException("Failed to generate cards validate input and try again.");
        }
    }
}
