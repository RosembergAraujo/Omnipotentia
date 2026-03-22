package com.berg.shopping.prices.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record StorePriceSummary(
        String storeName,
        List<ItemPriceDetail> items,
        BigDecimal estimatedTotal
) {}
