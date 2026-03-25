package com.berg.ai.adapter.in.rest.dto;

import com.berg.ai.domain.model.ExtractedItem;
import com.berg.ai.domain.model.ExtractedPrice;
import com.berg.ai.domain.model.IntentType;
import com.berg.ai.domain.model.ParsedMessage;

import java.util.List;

public record ParseMessageResponse(
        IntentType intent,
        List<ExtractedItem> items,
        ExtractedPrice priceInfo
) {
    public static ParseMessageResponse from(ParsedMessage domain) {
        return new ParseMessageResponse(
                domain.getIntent(),
                domain.getItems(),
                domain.getPriceInfo()
        );
    }
}
