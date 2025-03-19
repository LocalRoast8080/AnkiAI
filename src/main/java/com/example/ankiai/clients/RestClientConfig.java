package com.example.ankiai.clients;

import com.example.ankiai.configuration.XAiConfigProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final XAiConfigProperties xAiConfigProperties;

    public RestClientConfig(XAiConfigProperties xAiConfigProperties) {
        this.xAiConfigProperties = xAiConfigProperties;
    }

    @Bean
    @Qualifier("ankiConnectClient")
    public RestClient ankiRestClient(){
        return RestClient.builder()
                .baseUrl("http://localhost:8765")
                .build();
    }

    @Bean
    @Qualifier("grokClient")
    public RestClient grokAiClient(){
        return RestClient.builder()
                .baseUrl(xAiConfigProperties.url())
                .defaultHeader("Authorization", xAiConfigProperties.apiKey())
                .build();
    }

}
