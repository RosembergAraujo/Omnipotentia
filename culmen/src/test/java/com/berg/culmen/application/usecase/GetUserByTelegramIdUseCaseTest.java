package com.berg.culmen.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.berg.common.exception.NotFoundException;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetUserByTelegramIdUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private GetUserByTelegramIdUseCase useCase;

    @Test
    void shouldReturnUserWhenFound() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .telegramId("42")
                .telegramUsername("berg")
                .isActive(true)
                .build();
        when(userRepository.findByTelegramId("42")).thenReturn(Optional.of(user));

        User result = useCase.execute("42");

        assertThat(result.getTelegramId()).isEqualTo("42");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByTelegramId("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("nonexistent"))
                .isInstanceOf(NotFoundException.class);
    }
}
