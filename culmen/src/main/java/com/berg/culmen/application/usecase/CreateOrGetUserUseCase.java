package com.berg.culmen.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.berg.culmen.application.command.CreateUserCommand;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.in.CreateOrGetUserPort;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrGetUserUseCase implements CreateOrGetUserPort {

    private final UserRepositoryPort userRepository;

    @Override
    @Transactional
    public User execute(CreateUserCommand command) {
        return userRepository.findByTelegramId(command.telegramId())
                .orElseGet(() -> {
                    log.info("Creating new user telegramId={}", command.telegramId());
                    User newUser = User.create(
                            command.telegramId(),
                            command.telegramUsername(),
                            command.phoneNumber(),
                            command.displayName());
                    return userRepository.save(newUser);
                });
    }
}
