package com.berg.culmen.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String telegramId,
        String telegramUsername,
        String phoneNumber,
        String displayName
) {}
