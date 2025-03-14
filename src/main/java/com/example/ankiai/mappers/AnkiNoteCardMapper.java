package com.example.ankiai.mappers;

import com.example.ankiai.models.AnkiCreateRequest;
import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.models.AnkiNoteCardFull;
import com.example.ankiai.models.AnkiNoteUpdateReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnkiNoteCardMapper {

    @Mapping(target = "noteId", source = "noteId")
    @Mapping(target = "front", expression = "java(fullCard.getFields() != null && fullCard.getFields().getFront().getValue() != null ? fullCard.getFields().getFront().getValue() : null)")
    @Mapping(target = "back", expression = "java(fullCard.getFields() != null && fullCard.getFields().getBack().getValue() != null ? fullCard.getFields().getBack().getValue() : null)")
    AnkiNoteCard mapToAnkiNoteCard(AnkiNoteCardFull fullCard);

    List<AnkiNoteCard> mapToAnkiNoteCards(List<AnkiNoteCardFull> fullCards);

    @Mapping(target = "id", source = "noteId")
    @Mapping(target = "fields.front", source = "front")
    @Mapping(target = "fields.back", source = "back")
    AnkiNoteUpdateReq mapToAnkiUpdateReq(AnkiNoteCard noteCard);

    @Mapping(target = "deckName", source = "deckName")
    @Mapping(target = "fields.front", source = "noteCard.front")
    @Mapping(target = "fields.back", source = "noteCard.back")
    AnkiCreateRequest mapToAnkiCreateReq(AnkiNoteCard noteCard, String deckName);

    default List<AnkiCreateRequest> mapToAnkiCreateReqs(List<AnkiNoteCard> noteCards, String deckName) {
        List<AnkiCreateRequest> createRequests = new ArrayList<>();
        for (AnkiNoteCard noteCard : noteCards) {
            createRequests.add(mapToAnkiCreateReq(noteCard, deckName));
        }
        return createRequests;
    }
}
