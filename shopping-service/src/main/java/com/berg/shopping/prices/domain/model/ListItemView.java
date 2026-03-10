package com.berg.shopping.prices.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ListItemView(UUID id, String name, BigDecimal quantity, boolean checked) {}
