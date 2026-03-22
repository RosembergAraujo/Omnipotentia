package com.berg.shopping.prices.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record ReportPriceCommand(String productName, String storeName, BigDecimal price, UUID reportedBy) {}
