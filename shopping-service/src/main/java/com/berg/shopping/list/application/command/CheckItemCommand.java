package com.berg.shopping.list.application.command;

import java.util.UUID;

public record CheckItemCommand(UUID listId, UUID itemId, UUID ownerId) {}
