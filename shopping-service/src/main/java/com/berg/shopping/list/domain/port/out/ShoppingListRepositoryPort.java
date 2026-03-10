package com.berg.shopping.list.domain.port.out;

import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.model.ShoppingListItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListRepositoryPort {

    ShoppingList save(ShoppingList list);

    Optional<ShoppingList> findByIdAndOwnerId(UUID listId, UUID ownerId);

    List<ShoppingList> findAllByOwnerId(UUID ownerId);

    ShoppingListItem saveItem(ShoppingListItem item);

    Optional<ShoppingListItem> findItemByIdAndListId(UUID itemId, UUID listId);
}
