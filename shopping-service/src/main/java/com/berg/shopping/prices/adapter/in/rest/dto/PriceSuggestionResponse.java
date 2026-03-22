package com.berg.shopping.prices.adapter.in.rest.dto;

import com.berg.shopping.prices.application.dto.ItemPriceDetail;
import com.berg.shopping.prices.application.dto.StorePriceSummary;

import java.math.BigDecimal;
import java.util.List;

public record PriceSuggestionResponse(
        String storeName,
        List<ItemPriceDetail> items,
        BigDecimal estimatedTotal
) {
    public static PriceSuggestionResponse from(StorePriceSummary summary) {
        return new PriceSuggestionResponse(
                summary.storeName(),
                summary.items(),
                summary.estimatedTotal()
        );
    }
}
