package com.berg.common.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.berg.common.security.strategy.GenericJwtStrategy;
import com.berg.common.security.strategy.JwtAuthStrategy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private final List<JwtAuthStrategy> strategies;
    private final GenericJwtStrategy genericStrategy;

    public JwtUtil(List<JwtAuthStrategy> strategies, GenericJwtStrategy genericStrategy) {
        this.strategies = strategies;
        this.genericStrategy = genericStrategy;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private JwtAuthStrategy findStrategy(String authType) {
        if (authType == null)
            return genericStrategy;
        return strategies.stream()
                .filter(s -> s.getAuthType().equals(authType))
                .findFirst()
                .orElse(genericStrategy);
    }

    public String generateToken(UUID userId, String identifier, String authType) {
        JwtAuthStrategy strategy = findStrategy(authType);
        Map<String, Object> claims = strategy.buildClaims(userId, identifier);
        return Jwts.builder()
                .claims(claims)
                .subject(identifier)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public JwtAuthenticationToken extractAndCreateToken(String token) {
        Claims claims = extractClaims(token);
        String authType = claims.get("authType", String.class);
        JwtAuthStrategy strategy = findStrategy(authType);
        AuthIdentity identity = strategy.extractIdentity(claims);
        return strategy.createAuthToken(identity);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
