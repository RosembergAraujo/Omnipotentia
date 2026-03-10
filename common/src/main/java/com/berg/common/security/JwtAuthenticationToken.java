package com.berg.common.security;

import java.util.Collections;
import java.util.UUID;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthIdentity identity;

    public JwtAuthenticationToken(AuthIdentity identity) {
        super(Collections.emptyList());
        this.identity = identity;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return identity.userId();
    }

    public UUID getUserId() {
        return identity.userId();
    }

    public String getIdentifier() {
        return identity.identifier();
    }

    public String getAuthType() {
        return identity.authType();
    }

    public AuthIdentity getIdentity() {
        return identity;
    }
}
