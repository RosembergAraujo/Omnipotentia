package com.berg.shopping.list.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AddItemRequest(
        @NotBlank @Size(min = 1, max = 255) String name,
        @Positive BigDecimal quantity,
        @Size(max = 50) String unit) {}
