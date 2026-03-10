package com.berg.culmen.domain.port.out;

import java.util.Optional;

import com.berg.culmen.domain.model.User;

public interface UserRepositoryPort {

    Optional<User> findByTelegramId(String telegramId);

    User save(User user);
}
