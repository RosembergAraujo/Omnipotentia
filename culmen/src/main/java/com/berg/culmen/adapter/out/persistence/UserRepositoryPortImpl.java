package com.berg.culmen.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryPortImpl implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Lazy
    @Autowired
    private UserRepositoryPortImpl self;

    @Override
    public Optional<User> findByTelegramId(String telegramId) {
        return Optional.ofNullable(self.findCached(telegramId));
    }

    @Cacheable(value = "users", key = "#telegramId", unless = "#result == null")
    public User findCached(String telegramId) {
        return jpaRepository.findByTelegramId(telegramId)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = "users", key = "#user.telegramId")
    public User save(User user) {
        UserJpaEntity entity = jpaRepository.save(mapper.toEntity(user));
        return mapper.toDomain(entity);
    }
}
