package com.berg.shopping.prices.domain.port.out;

import com.berg.shopping.prices.domain.model.ListItemView;

import java.util.List;
import java.util.UUID;

public interface ListItemQueryPort {

    List<ListItemView> findByListAndOwner(UUID listId, UUID ownerId);
}
