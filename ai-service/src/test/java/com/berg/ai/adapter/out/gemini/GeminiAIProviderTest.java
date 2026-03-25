package com.berg.ai.adapter.out.gemini;

import com.berg.ai.domain.model.IntentType;
import com.berg.ai.domain.model.ParsedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GeminiAIProviderTest {

    private MockWebServer mockWebServer;
    private GeminiAIProvider provider;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        provider = new GeminiAIProvider(webClient, new ObjectMapper());
        // inject apiKey and model via reflection
        setField(provider, "apiKey", "test-key");
        setField(provider, "model", "gemini-1.5-flash");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldParseAddItemsIntent() {
        String geminiBody = """
                {
                  "candidates": [{
                    "content": {
                      "parts": [{
                        "text": "{\\"intent\\":\\"ADD_ITEMS\\",\\"items\\":[{\\"name\\":\\"arroz\\",\\"quantity\\":2.0,\\"unit\\":\\"kg\\"}],\\"priceInfo\\":null}"
                      }]
                    }
                  }]
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(geminiBody)
                .addHeader("Content-Type", "application/json"));

        ParsedMessage result = provider.parse("adiciona 2kg de arroz");

        assertThat(result.getIntent()).isEqualTo(IntentType.ADD_ITEMS);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("arroz");
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(2.0);
    }

    @Test
    void shouldReturnUnknownForInvalidJson() {
        String geminiBody = """
                {
                  "candidates": [{
                    "content": {
                      "parts": [{ "text": "não sei responder" }]
                    }
                  }]
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(geminiBody)
                .addHeader("Content-Type", "application/json"));

        ParsedMessage result = provider.parse("asdfghjkl");

        assertThat(result.getIntent()).isEqualTo(IntentType.UNKNOWN);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getPriceInfo()).isNull();
    }

    @Test
    void shouldStripMarkdownCodeBlock() {
        // Gemini sometimes wraps JSON in ```json ... ``` — the adapter must strip it
        String geminiBody = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":"
                + "\"```json\\n{\\\"intent\\\":\\\"SHOW_LIST\\\",\\\"items\\\":[],\\\"priceInfo\\\":null}\\n```\"}]}}]}";
        mockWebServer.enqueue(new MockResponse()
                .setBody(geminiBody)
                .addHeader("Content-Type", "application/json"));

        ParsedMessage result = provider.parse("mostra minha lista");

        assertThat(result.getIntent()).isEqualTo(IntentType.SHOW_LIST);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
