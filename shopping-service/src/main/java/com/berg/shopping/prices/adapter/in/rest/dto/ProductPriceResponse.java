package com.berg.shopping.prices.adapter.in.rest.dto;

import com.berg.shopping.prices.domain.model.ProductPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductPriceResponse(
        UUID id,
        String productName,
        String storeName,
        BigDecimal price,
        UUID reportedBy,
        LocalDateTime reportedAt
) {
    public static ProductPriceResponse from(ProductPrice price) {
        return new ProductPriceResponse(
                price.getId(),
                price.getProductName(),
                price.getStoreName(),
                price.getPrice(),
                price.getReportedBy(),
                price.getReportedAt()
        );
    }
}
