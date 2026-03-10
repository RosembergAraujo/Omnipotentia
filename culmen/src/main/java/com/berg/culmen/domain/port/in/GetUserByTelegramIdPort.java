package com.berg.culmen.domain.port.in;

import com.berg.culmen.domain.model.User;

public interface GetUserByTelegramIdPort {

    /**
     * Returns the user with the given telegramId.
     *
     * @throws com.berg.common.exception.NotFoundException if no user is found
     */
    User execute(String telegramId);
}
