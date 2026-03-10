package com.berg.shopping.list.application.usecase;

import com.berg.common.exception.NotFoundException;
import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.application.command.CheckItemCommand;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import com.berg.shopping.list.domain.port.in.CheckItemPort;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckItemUseCase implements CheckItemPort {

    private final ShoppingListRepositoryPort repository;

    @Override
    @Transactional
    public ShoppingListItem execute(CheckItemCommand command) {
        repository.findByIdAndOwnerId(command.listId(), command.ownerId())
                .orElseThrow(() -> new UnauthorizedException("List not found or access denied"));

        ShoppingListItem item = repository.findItemByIdAndListId(command.itemId(), command.listId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        return repository.saveItem(item.toggleChecked());
    }
}
