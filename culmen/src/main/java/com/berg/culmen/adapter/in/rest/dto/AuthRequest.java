package com.berg.culmen.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String telegramId,
        String telegramUsername
) {}
