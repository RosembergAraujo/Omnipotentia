package com.berg.culmen.adapter.in.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.berg.culmen.domain.model.User;

public record UserResponse(
        UUID id,
        String telegramId,
        String telegramUsername,
        String displayName,
        Boolean isActive,
        LocalDateTime createdAt) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getTelegramId(),
                user.getTelegramUsername(),
                user.getDisplayName(),
                user.getIsActive(),
                user.getCreatedAt());
    }
}
