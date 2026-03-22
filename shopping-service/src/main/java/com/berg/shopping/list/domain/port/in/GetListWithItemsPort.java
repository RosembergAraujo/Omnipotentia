package com.berg.shopping.list.domain.port.in;

import com.berg.shopping.list.domain.model.ShoppingList;

import java.util.List;
import java.util.UUID;

public interface GetListWithItemsPort {

    ShoppingList execute(UUID listId, UUID ownerId);

    List<ShoppingList> findAllByOwner(UUID ownerId);
}
