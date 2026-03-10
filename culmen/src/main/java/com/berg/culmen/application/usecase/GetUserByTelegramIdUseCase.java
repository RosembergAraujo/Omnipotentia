package com.berg.culmen.application.usecase;

import org.springframework.stereotype.Service;

import com.berg.common.exception.NotFoundException;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.in.GetUserByTelegramIdPort;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserByTelegramIdUseCase implements GetUserByTelegramIdPort {

    private final UserRepositoryPort userRepository;

    @Override
    public User execute(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new NotFoundException("User", telegramId));
    }
}
