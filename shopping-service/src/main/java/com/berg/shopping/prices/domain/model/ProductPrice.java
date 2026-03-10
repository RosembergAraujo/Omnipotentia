package com.berg.shopping.prices.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductPrice {

    private UUID id;
    private String productName;
    private String storeName;
    private BigDecimal price;
    private UUID reportedBy;
    private LocalDateTime reportedAt;

    public static ProductPrice create(String productName, String storeName, BigDecimal price, UUID reportedBy) {
        return ProductPrice.builder()
                .productName(productName.toLowerCase().trim())
                .storeName(storeName.trim())
                .price(price)
                .reportedBy(reportedBy)
                .build();
    }
}
