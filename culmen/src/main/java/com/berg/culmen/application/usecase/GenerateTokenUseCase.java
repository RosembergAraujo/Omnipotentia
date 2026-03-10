package com.berg.culmen.application.usecase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.berg.common.security.JwtUtil;
import com.berg.culmen.application.command.GenerateTokenCommand;
import com.berg.culmen.application.dto.AuthTokenResponse;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.in.GenerateTokenPort;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.berg.common.security.strategy.TelegramJwtStrategy;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateTokenUseCase implements GenerateTokenPort {

    private final UserRepositoryPort userRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}")
    private long expirationMs;

    @Override
    @Transactional
    public AuthTokenResponse execute(GenerateTokenCommand command) {
        User user = userRepository.findByTelegramId(command.telegramId())
                .orElseGet(() -> {
                    log.info("Auto-registering user telegramId={}", command.telegramId());
                    User newUser = User.create(
                            command.telegramId(),
                            command.telegramUsername(),
                            null,
                            command.telegramUsername());
                    return userRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(user.getId(), user.getTelegramId(), TelegramJwtStrategy.AUTH_TYPE);
        long expiresIn = expirationMs / 1000;

        log.debug("Token generated for userId={}", user.getId());
        return new AuthTokenResponse(token, user.getId(), expiresIn);
    }
}
