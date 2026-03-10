package com.berg.common.security.strategy;

import java.util.Map;
import java.util.UUID;

import com.berg.common.security.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import com.berg.common.security.AuthIdentity;

public interface JwtAuthStrategy {

    String getAuthType();

    Map<String, Object> buildClaims(UUID userId, String identifier);

    AuthIdentity extractIdentity(Claims claims);

    JwtAuthenticationToken createAuthToken(AuthIdentity identity);
}
