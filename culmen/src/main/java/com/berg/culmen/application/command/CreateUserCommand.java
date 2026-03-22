package com.berg.culmen.application.command;

public record CreateUserCommand(
        String telegramId,
        String telegramUsername,
        String phoneNumber,
        String displayName
) {}
