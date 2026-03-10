package com.berg.common.security;

import java.util.UUID;

public record AuthIdentity(UUID userId, String identifier, String authType) {
}
