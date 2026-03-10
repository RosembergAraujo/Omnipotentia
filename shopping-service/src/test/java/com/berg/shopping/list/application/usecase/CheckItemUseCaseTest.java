package com.berg.shopping.list.application.usecase;

import com.berg.common.exception.NotFoundException;
import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.application.command.CheckItemCommand;
import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckItemUseCaseTest {

    @Mock
    private ShoppingListRepositoryPort repository;

    @InjectMocks
    private CheckItemUseCase useCase;

    @Test
    void shouldToggleItemToChecked() {
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        ShoppingList list = ShoppingList.builder().id(listId).ownerId(ownerId).name("Lista").build();
        ShoppingListItem item = ShoppingListItem.builder().id(itemId).listId(listId).name("Leite").checked(false).build();
        ShoppingListItem toggled = item.toggleChecked();

        when(repository.findByIdAndOwnerId(listId, ownerId)).thenReturn(Optional.of(list));
        when(repository.findItemByIdAndListId(itemId, listId)).thenReturn(Optional.of(item));
        when(repository.saveItem(any(ShoppingListItem.class))).thenReturn(toggled);

        ShoppingListItem result = useCase.execute(new CheckItemCommand(listId, itemId, ownerId));

        assertThat(result.isChecked()).isTrue();
    }

    @Test
    void shouldThrowUnauthorizedWhenListDoesNotBelongToOwner() {
        UUID listId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        when(repository.findByIdAndOwnerId(listId, ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.execute(new CheckItemCommand(listId, UUID.randomUUID(), ownerId)))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void shouldThrowNotFoundWhenItemDoesNotExist() {
        UUID listId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        ShoppingList list = ShoppingList.builder().id(listId).ownerId(ownerId).name("Lista").build();

        when(repository.findByIdAndOwnerId(listId, ownerId)).thenReturn(Optional.of(list));
        when(repository.findItemByIdAndListId(itemId, listId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.execute(new CheckItemCommand(listId, itemId, ownerId)))
                .isInstanceOf(NotFoundException.class);
    }
}
