package com.berg.culmen.domain.port.in;

import com.berg.culmen.application.command.CreateUserCommand;
import com.berg.culmen.domain.model.User;

public interface CreateOrGetUserPort {

    /**
     * Creates a new user or returns the existing one for the given telegramId.
     */
    User execute(CreateUserCommand command);
}
