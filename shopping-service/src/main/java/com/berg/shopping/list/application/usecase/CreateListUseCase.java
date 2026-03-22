package com.berg.shopping.list.application.usecase;

import com.berg.shopping.list.application.command.CreateListCommand;
import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.port.in.CreateListPort;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateListUseCase implements CreateListPort {

    private final ShoppingListRepositoryPort repository;

    @Override
    @Transactional
    public ShoppingList execute(CreateListCommand command) {
        ShoppingList list = ShoppingList.create(command.name(), command.ownerId());
        return repository.save(list);
    }
}
