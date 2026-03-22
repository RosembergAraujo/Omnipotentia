package com.berg.shopping.list.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record AddItemCommand(UUID listId, UUID ownerId, String name, BigDecimal quantity, String unit) {}
