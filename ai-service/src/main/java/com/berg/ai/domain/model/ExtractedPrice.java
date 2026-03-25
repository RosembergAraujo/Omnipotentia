package com.berg.ai.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExtractedPrice {
    String productName;
    String storeName;
    Double price;
}
