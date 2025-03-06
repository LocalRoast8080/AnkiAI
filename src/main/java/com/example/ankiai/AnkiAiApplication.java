package com.example.ankiai;

import com.example.ankiai.configuration.XAiConfigProperties;
import com.example.ankiai.services.NoteCardProcessorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(XAiConfigProperties.class)
public class AnkiAiApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AnkiAiApplication.class, args);
		NoteCardProcessorService noteService = context.getBean(NoteCardProcessorService.class);

//		var noteCards = noteService.getNoteCards("AppTesting", 100);
//		var spellCheckedCards = noteService.spellCheck(noteCards);
//		var expandedCards = noteService.expandAnswers(spellCheckedCards);
//
//		System.out.println(expandedCards.toString());
	}
}
