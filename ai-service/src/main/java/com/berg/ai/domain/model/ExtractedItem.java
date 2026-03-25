package com.berg.ai.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExtractedItem {
    String name;
    Double quantity;
    String unit;
}
