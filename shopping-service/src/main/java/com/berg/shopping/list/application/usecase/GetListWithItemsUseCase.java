package com.berg.shopping.list.application.usecase;

import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.port.in.GetListWithItemsPort;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetListWithItemsUseCase implements GetListWithItemsPort {

    private final ShoppingListRepositoryPort repository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingList execute(UUID listId, UUID ownerId) {
        return repository.findByIdAndOwnerId(listId, ownerId)
                .orElseThrow(() -> new UnauthorizedException("List not found or access denied"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingList> findAllByOwner(UUID ownerId) {
        return repository.findAllByOwnerId(ownerId);
    }
}
