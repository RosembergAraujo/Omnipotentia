package com.berg.culmen.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = User.UserBuilder.class)
public class User {

    private UUID id;
    private String telegramId;
    private String telegramUsername;
    private String phoneNumber;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private Boolean isActive = true;

    public static User create(String telegramId, String telegramUsername,
            String phoneNumber, String displayName) {
        return User.builder()
                .telegramId(telegramId)
                .telegramUsername(telegramUsername)
                .phoneNumber(phoneNumber)
                .displayName(displayName)
                .isActive(true)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class UserBuilder {
    }
}
