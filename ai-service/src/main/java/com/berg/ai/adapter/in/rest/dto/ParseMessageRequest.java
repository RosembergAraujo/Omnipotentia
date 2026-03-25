package com.berg.ai.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ParseMessageRequest(
        @NotBlank(message = "message must not be blank")
        String message
) {}
