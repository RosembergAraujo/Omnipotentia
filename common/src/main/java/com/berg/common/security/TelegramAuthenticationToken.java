package com.berg.common.security;

public class TelegramAuthenticationToken extends JwtAuthenticationToken {

    public TelegramAuthenticationToken(AuthIdentity identity) {
        super(identity);
    }

    public String getTelegramId() {
        return getIdentifier();
    }
}
