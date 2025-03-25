// package UnitTests.Services;

// import com.example.ankiai.models.AnkiDeck;
// import com.example.ankiai.models.AnkiResponse;
// import com.example.ankiai.mappers.AnkiNoteCardMapper;
// import com.example.ankiai.services.AnkiService;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.web.client.RestClient;
// import com.fasterxml.jackson.core.type.TypeReference;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class AnkiServiceUnitTests {

//     @Mock
//     private RestClient ankiClient;
//     @Mock
//     private AnkiNoteCardMapper ankiMapper;
//     @Mock
//     private ObjectMapper mapper;

//     @InjectMocks
//     private AnkiService ankiService;

//     @BeforeEach
//     void setUp() {

//     }

//     @Test
//     void testGetDecks() throws JsonProcessingException {

//         // Arrange
//         RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
//         RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
//         RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
//         AnkiResponse<List<AnkiDeck>> mockResponse = new AnkiResponse<>();
//         mockResponse.result = List.of(
//                 new AnkiDeck("Deck1"),
//                 new AnkiDeck("Deck2"),
//                 new AnkiDeck("Deck3"),
//                 new AnkiDeck("Deck4"),
//                 new AnkiDeck("Deck5")
//         );
//         mockResponse.error = null;

//         // Act
//         when(ankiClient.post()).thenReturn(requestBodyUriSpec);
//         when(requestBodyUriSpec.uri("/")).thenReturn(requestBodySpec);
//         when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec); // Fixed stubbing
//         when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//         when(responseSpec.body(String.class)).thenReturn("some response");

//         // Assert
//         when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(mockResponse);
//         List<AnkiDeck> decks = ankiService.getDecks();
//         assertEquals(5 , decks.size());
//         // verify mapper
//         // verify(ankiService, times).getDecks();
//         //verify(mapper,times(1)).readValue(anyString(), eq(AnkiResponse.class));
//         //https://www.baeldung.com/mockito-mock-jackson-read-value
//     }
// }
