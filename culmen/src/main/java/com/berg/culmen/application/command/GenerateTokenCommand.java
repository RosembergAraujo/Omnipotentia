package com.berg.culmen.application.command;

public record GenerateTokenCommand(
        String telegramId,
        String telegramUsername
) {}
