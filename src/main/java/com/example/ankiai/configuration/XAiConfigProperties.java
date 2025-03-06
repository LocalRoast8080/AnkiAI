package com.example.ankiai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xapi")
public record XAiConfigProperties(String apiKey, String url) {
}
