package com.berg.culmen.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.berg.culmen.application.command.CreateUserCommand;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreateOrGetUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private CreateOrGetUserUseCase useCase;

    @Test
    void shouldReturnExistingUserWhenTelegramIdAlreadyExists() {
        User existingUser = User.builder()
                .id(UUID.randomUUID())
                .telegramId("123456")
                .telegramUsername("john")
                .isActive(true)
                .build();
        when(userRepository.findByTelegramId("123456")).thenReturn(Optional.of(existingUser));

        User result = useCase.execute(new CreateUserCommand("123456", "john", null, "John"));

        assertThat(result).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldCreateAndSaveNewUserWhenTelegramIdDoesNotExist() {
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .telegramId("999")
                .telegramUsername("newuser")
                .displayName("New User")
                .isActive(true)
                .build();
        when(userRepository.findByTelegramId("999")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = useCase.execute(new CreateUserCommand("999", "newuser", null, "New User"));

        assertThat(result.getTelegramId()).isEqualTo("999");
        verify(userRepository).save(any(User.class));
    }
}
