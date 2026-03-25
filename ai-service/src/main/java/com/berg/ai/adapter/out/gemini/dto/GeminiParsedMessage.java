package com.berg.ai.adapter.out.gemini.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiParsedMessage(
        String intent,
        List<ItemDto> items,
        PriceDto priceInfo
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ItemDto(String name, Double quantity, String unit) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PriceDto(String productName, String storeName, Double price) {}
}
