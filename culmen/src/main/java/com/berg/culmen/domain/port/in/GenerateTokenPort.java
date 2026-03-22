package com.berg.culmen.domain.port.in;

import com.berg.culmen.application.command.GenerateTokenCommand;
import com.berg.culmen.application.dto.AuthTokenResponse;

public interface GenerateTokenPort {

    /**
     * Finds or creates the user for the given telegramId and returns a signed JWT.
     */
    AuthTokenResponse execute(GenerateTokenCommand command);
}
