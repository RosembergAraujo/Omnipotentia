package com.berg.shopping.list.application.usecase;

import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.application.command.AddItemCommand;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import com.berg.shopping.list.domain.port.in.AddItemToListPort;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddItemToListUseCase implements AddItemToListPort {

    private final ShoppingListRepositoryPort repository;

    @Override
    @Transactional
    public ShoppingListItem execute(AddItemCommand command) {
        repository.findByIdAndOwnerId(command.listId(), command.ownerId())
                .orElseThrow(() -> new UnauthorizedException("List not found or access denied"));

        ShoppingListItem item = ShoppingListItem.create(
                command.listId(), command.name(), command.quantity(), command.unit());

        return repository.saveItem(item);
    }
}
