package com.example.ankiai.mappers;

import com.example.ankiai.models.AnkiNoteCard;
import com.example.ankiai.models.AnkiNoteCardFull;
import com.example.ankiai.models.AnkiNoteUpdateReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnkiNoteCardMapper {

    @Mapping(target = "noteId", source = "noteId")
    @Mapping(target = "front", expression = "java(fullCard.getFields() != null && fullCard.getFields().getFront() != null ? fullCard.getFields().getFront() : null)")
    @Mapping(target = "back", expression = "java(fullCard.getFields() != null && fullCard.getFields().getBack() != null ? fullCard.getFields().getBack() : null)")
    AnkiNoteCard mapToAnkiNoteCard(AnkiNoteCardFull fullCard);

    List<AnkiNoteCard> mapToAnkiNoteCards(List<AnkiNoteCardFull> fullCards);

    @Mapping(target = "id", source = "noteId")
    @Mapping(target = "fields.front", source = "front")
    @Mapping(target = "fields.back", source = "back")
    AnkiNoteUpdateReq mapToAnkiNoteReq(AnkiNoteCard noteCard);
}
