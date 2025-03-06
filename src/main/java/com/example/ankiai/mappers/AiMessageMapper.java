package com.example.ankiai.mappers;

import com.example.ankiai.models.AiResponseMessage;
import com.example.ankiai.models.AnkiNoteCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AiMessageMapper {

    AnkiNoteCard mapToAnkiNoteCard(AiResponseMessage message);
}
