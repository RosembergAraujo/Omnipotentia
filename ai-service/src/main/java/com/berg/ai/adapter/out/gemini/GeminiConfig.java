package com.berg.ai.adapter.out.gemini;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiConfig {

    private final String baseUrl;

    public GeminiConfig(@Value("${gemini.api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
