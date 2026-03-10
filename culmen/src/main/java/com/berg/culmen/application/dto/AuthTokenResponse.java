package com.berg.culmen.application.dto;

import java.util.UUID;

public record AuthTokenResponse(
                String token,
                UUID userId,
                long expiresIn) {
}
