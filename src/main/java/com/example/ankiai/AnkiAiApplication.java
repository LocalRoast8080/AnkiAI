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

    public static void main(String[] args) { SpringApplication.run(AnkiAiApplication.class, args);}
}
