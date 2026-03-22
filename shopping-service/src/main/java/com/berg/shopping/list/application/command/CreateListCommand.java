package com.berg.shopping.list.application.command;

import java.util.UUID;

public record CreateListCommand(String name, UUID ownerId) {}
