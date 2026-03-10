package com.berg.shopping.prices.adapter.out.list;

import com.berg.common.exception.UnauthorizedException;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import com.berg.shopping.prices.domain.model.ListItemView;
import com.berg.shopping.prices.domain.port.out.ListItemQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListItemQueryAdapter implements ListItemQueryPort {

    private final ShoppingListRepositoryPort shoppingListRepository;

    @Override
    public List<ListItemView> findByListAndOwner(UUID listId, UUID ownerId) {
        return shoppingListRepository.findByIdAndOwnerId(listId, ownerId)
                .orElseThrow(() -> new UnauthorizedException("List not found or access denied"))
                .getItems().stream()
                .map(item -> new ListItemView(item.getId(), item.getName(), item.getQuantity(), item.isChecked()))
                .toList();
    }
}
