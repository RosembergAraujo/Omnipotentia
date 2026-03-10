package com.berg.culmen.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.berg.common.security.JwtUtil;
import com.berg.culmen.application.command.GenerateTokenCommand;
import com.berg.culmen.application.dto.AuthTokenResponse;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GenerateTokenUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private GenerateTokenUseCase useCase;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(Objects.requireNonNull(useCase), "expirationMs", 86400000L);
    }

    @Test
    void shouldGenerateTokenForExistingUser() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.builder()
                .id(userId)
                .telegramId("123")
                .telegramUsername("berg")
                .isActive(true)
                .build();
        when(userRepository.findByTelegramId("123")).thenReturn(Optional.of(existingUser));
        when(jwtUtil.generateToken(eq(userId), eq("123"), any())).thenReturn("jwt-token");

        AuthTokenResponse response = useCase.execute(new GenerateTokenCommand("123", "berg"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.expiresIn()).isEqualTo(86400L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldAutoRegisterAndGenerateTokenForNewUser() {
        UUID userId = UUID.randomUUID();
        User savedUser = User.builder()
                .id(userId)
                .telegramId("777")
                .telegramUsername("newbie")
                .isActive(true)
                .build();
        when(userRepository.findByTelegramId("777")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(eq(userId), eq("777"), any())).thenReturn("new-jwt-token");

        AuthTokenResponse response = useCase.execute(new GenerateTokenCommand("777", "newbie"));

        assertThat(response.token()).isEqualTo("new-jwt-token");
        assertThat(response.userId()).isEqualTo(userId);
        verify(userRepository).save(any(User.class));
    }
}
