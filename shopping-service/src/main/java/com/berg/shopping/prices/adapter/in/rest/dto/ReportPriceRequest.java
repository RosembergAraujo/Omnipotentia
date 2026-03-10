package com.berg.shopping.prices.adapter.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ReportPriceRequest(
        @NotBlank @Size(min = 1, max = 255) String productName,
        @NotBlank @Size(min = 1, max = 255) String storeName,
        @NotNull @DecimalMin("0.01") BigDecimal price) {}
