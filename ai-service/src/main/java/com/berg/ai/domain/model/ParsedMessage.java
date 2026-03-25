package com.berg.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ParsedMessage {
    IntentType intent;
    List<ExtractedItem> items;
    ExtractedPrice priceInfo;

    public static ParsedMessage unknown() {
        return ParsedMessage.builder()
                .intent(IntentType.UNKNOWN)
                .items(List.of())
                .priceInfo(null)
                .build();
    }
}
