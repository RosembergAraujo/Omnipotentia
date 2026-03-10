package com.berg.shopping.list.application.usecase;

import com.berg.shopping.list.application.command.CreateListCommand;
import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateListUseCaseTest {

    @Mock
    private ShoppingListRepositoryPort repository;

    @InjectMocks
    private CreateListUseCase useCase;

    @Test
    void shouldCreateAndSaveList() {
        UUID ownerId = UUID.randomUUID();
        ShoppingList saved = ShoppingList.builder()
                .id(UUID.randomUUID())
                .name("Mercado")
                .ownerId(ownerId)
                .build();

        when(repository.save(any(ShoppingList.class))).thenReturn(saved);

        ShoppingList result = useCase.execute(new CreateListCommand("Mercado", ownerId));

        assertThat(result.getName()).isEqualTo("Mercado");
        assertThat(result.getOwnerId()).isEqualTo(ownerId);
        verify(repository).save(any(ShoppingList.class));
    }
}
