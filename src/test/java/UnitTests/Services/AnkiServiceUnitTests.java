package UnitTests.Services;

import com.example.ankiai.clients.RestClientConfig;
import com.example.ankiai.models.AnkiAction;
import com.example.ankiai.models.AnkiDeck;
import com.example.ankiai.models.AnkiResponse;
import com.example.ankiai.mappers.AnkiNoteCardMapper;
import com.example.ankiai.services.AnkiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnkiServiceUnitTests {

    @Mock
    private RestClient ankiClient;
    @Mock
    private AnkiNoteCardMapper ankiMapper;
    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private AnkiService ankiService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetDecks() throws JsonProcessingException {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Stub the chain
        when(ankiClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec); // Fixed stubbing
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("some response");

        AnkiResponse<List<AnkiDeck>> mockResponse = new AnkiResponse<>();
        mockResponse.result = List.of(
                new AnkiDeck("Deck1"),
                new AnkiDeck("Deck2"),
                new AnkiDeck("Deck3"),
                new AnkiDeck("Deck4"),
                new AnkiDeck("Deck5")
        );
        mockResponse.error = null;

        // Step 3: Mock ObjectMapper.readValue
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(mockResponse);
        // Act

        List<AnkiDeck> decks = ankiService.getDecks();
        // Assert
        assertEquals(5 , decks.size());
        // verify mapper
//        verify(ankiService, times).getDecks();
        //verify(mapper,times(1)).readValue(anyString(), eq(AnkiResponse.class));
        //https://www.baeldung.com/mockito-mock-jackson-read-value
    }
}
