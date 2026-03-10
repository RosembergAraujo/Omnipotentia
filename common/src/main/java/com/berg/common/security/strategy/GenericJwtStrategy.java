package com.berg.common.security.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.berg.common.security.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import com.berg.common.security.AuthIdentity;

@Component
public class GenericJwtStrategy implements JwtAuthStrategy {

    public static final String AUTH_TYPE = "generic";

    @Override
    public String getAuthType() {
        return AUTH_TYPE;
    }

    @Override
    public Map<String, Object> buildClaims(UUID userId, String identifier) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("authType", AUTH_TYPE);
        return claims;
    }

    @Override
    public AuthIdentity extractIdentity(Claims claims) {
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        String identifier = claims.getSubject();
        return new AuthIdentity(userId, identifier, AUTH_TYPE);
    }

    @Override
    public JwtAuthenticationToken createAuthToken(AuthIdentity identity) {
        return new JwtAuthenticationToken(identity);
    }
}
