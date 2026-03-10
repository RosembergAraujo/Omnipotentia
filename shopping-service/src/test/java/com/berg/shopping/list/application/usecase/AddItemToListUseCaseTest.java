package com.berg.shopping.list.application.usecase;

import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.application.command.AddItemCommand;
import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddItemToListUseCaseTest {

    @Mock
    private ShoppingListRepositoryPort repository;

    @InjectMocks
    private AddItemToListUseCase useCase;

    @Test
    void shouldAddItemWhenListBelongsToOwner() {
        UUID listId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        ShoppingList list = ShoppingList.builder().id(listId).name("Lista").ownerId(ownerId).build();
        BigDecimal two = BigDecimal.valueOf(2);
        ShoppingListItem savedItem = ShoppingListItem.builder()
                .id(UUID.randomUUID()).listId(listId).name("Arroz").quantity(two).build();

        when(repository.findByIdAndOwnerId(listId, ownerId)).thenReturn(Optional.of(list));
        when(repository.saveItem(any(ShoppingListItem.class))).thenReturn(savedItem);

        ShoppingListItem result = useCase.execute(new AddItemCommand(listId, ownerId, "Arroz", two, null));

        assertThat(result.getName()).isEqualTo("Arroz");
        assertThat(result.getQuantity()).isEqualByComparingTo(two);
        verify(repository).saveItem(any(ShoppingListItem.class));
    }

    @Test
    void shouldThrowUnauthorizedWhenListDoesNotBelongToOwner() {
        UUID listId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        when(repository.findByIdAndOwnerId(listId, ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.execute(new AddItemCommand(listId, ownerId, "Feijão", BigDecimal.ONE, null)))
                .isInstanceOf(UnauthorizedException.class);
    }
}
