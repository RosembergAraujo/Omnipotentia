package com.berg.ai.adapter.out.gemini;

import com.berg.ai.adapter.out.gemini.dto.GeminiParsedMessage;
import com.berg.ai.adapter.out.gemini.dto.GeminiRequest;
import com.berg.ai.adapter.out.gemini.dto.GeminiResponse;
import com.berg.ai.domain.model.ExtractedItem;
import com.berg.ai.domain.model.ExtractedPrice;
import com.berg.ai.domain.model.IntentType;
import com.berg.ai.domain.model.ParsedMessage;
import com.berg.ai.domain.port.out.AIProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiAIProvider implements AIProvider {

    private static final String PROMPT_TEMPLATE = """
            Você é um assistente EXCLUSIVAMENTE de lista de compras. \
            Seu único papel é interpretar mensagens relacionadas a compras e retornar SOMENTE um JSON válido \
            (sem markdown, sem explicações, sem texto fora do JSON).

            INTENÇÕES PERMITIDAS:
            - ADD_ITEMS: adicionar itens à lista (ex: "adiciona 2kg de arroz")
            - SHOW_LIST: visualizar a lista (ex: "mostra minha lista", "o que tenho na lista")
            - REPORT_PRICE: informar preço de um produto em uma loja (ex: "o arroz está R$5 no mercado X")
            - COMPARE_PRICES: comparar preços de um produto (ex: "onde está mais barato o feijão?")
            - HELP: quando o usuário perguntar o que você pode fazer (ex: "o que você faz?", "me ajuda", "help")
            - UNKNOWN: qualquer mensagem fora do escopo de lista de compras

            FORMATO DE RESPOSTA:
            {
              "intent": "<ADD_ITEMS|SHOW_LIST|REPORT_PRICE|COMPARE_PRICES|HELP|UNKNOWN>",
              "items": [{ "name": "...", "quantity": <número>, "unit": "..." }],
              "priceInfo": { "productName": "...", "storeName": "...", "price": <número> }
            }

            REGRAS OBRIGATÓRIAS:
            - Retorne APENAS o JSON, nunca texto adicional
            - "items" só quando intent=ADD_ITEMS, senão []
            - "priceInfo" só quando intent=REPORT_PRICE, senão null
            - nomes de produtos sempre em lowercase
            - Se a mensagem não for sobre lista de compras, use intent=UNKNOWN
            - Se o usuário pedir ajuda ou quiser saber o que você faz, use intent=HELP

            Mensagem: "%s"
            """;

    private final WebClient geminiWebClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model}")
    private String model;

    @Override
    public ParsedMessage parse(String message) {
        String prompt = PROMPT_TEMPLATE.formatted(message);
        String rawJson = callGemini(prompt);
        return parseResponse(rawJson);
    }

    private String callGemini(String prompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        GeminiResponse response = geminiWebClient.post()
                .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();

        if (response == null
                || response.candidates() == null
                || response.candidates().isEmpty()) {
            log.warn("Gemini returned empty response");
            return "";
        }

        return response.candidates().get(0).content().parts().get(0).text();
    }

    private ParsedMessage parseResponse(String raw) {
        try {
            String json = stripMarkdown(raw);
            GeminiParsedMessage parsed = objectMapper.readValue(json, GeminiParsedMessage.class);
            return toDomain(parsed);
        } catch (Exception e) {
            log.warn("Could not parse Gemini response as JSON, returning UNKNOWN. Raw: {}", raw);
            return ParsedMessage.unknown();
        }
    }

    private String stripMarkdown(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('\n') + 1;
            int end = trimmed.lastIndexOf("```");
            return trimmed.substring(start, end).trim();
        }
        return trimmed;
    }

    private ParsedMessage toDomain(GeminiParsedMessage parsed) {
        IntentType intent;
        try {
            intent = IntentType.valueOf(parsed.intent());
        } catch (IllegalArgumentException e) {
            intent = IntentType.UNKNOWN;
        }

        List<ExtractedItem> items = parsed.items() == null ? List.of() :
                parsed.items().stream()
                        .map(i -> ExtractedItem.builder()
                                .name(i.name())
                                .quantity(i.quantity())
                                .unit(i.unit())
                                .build())
                        .toList();

        ExtractedPrice priceInfo = null;
        if (parsed.priceInfo() != null) {
            priceInfo = ExtractedPrice.builder()
                    .productName(parsed.priceInfo().productName())
                    .storeName(parsed.priceInfo().storeName())
                    .price(parsed.priceInfo().price())
                    .build();
        }

        return ParsedMessage.builder()
                .intent(intent)
                .items(items)
                .priceInfo(priceInfo)
                .build();
    }

}
