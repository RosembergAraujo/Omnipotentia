package com.berg.shopping.list.adapter.out.persistence;

import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import com.berg.shopping.list.domain.port.out.ShoppingListRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShoppingListRepositoryAdapter implements ShoppingListRepositoryPort {

    private final ShoppingListJpaRepository listRepository;
    private final ShoppingListItemJpaRepository itemRepository;
    private final ShoppingListMapper mapper;

    @Override
    public ShoppingList save(ShoppingList list) {
        ShoppingListJpaEntity entity = mapper.toEntity(list);
        return mapper.toDomain(listRepository.save(entity));
    }

    @Override
    public Optional<ShoppingList> findByIdAndOwnerId(UUID listId, UUID ownerId) {
        return listRepository.findByIdAndOwnerId(listId, ownerId)
                .map(mapper::toDomain);
    }

    @Override
    public List<ShoppingList> findAllByOwnerId(UUID ownerId) {
        return listRepository.findAllByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public ShoppingListItem saveItem(ShoppingListItem item) {
        ShoppingListJpaEntity listRef = listRepository.getReferenceById(item.getListId());
        ShoppingListItemJpaEntity entity = ShoppingListItemJpaEntity.builder()
                .id(item.getId())
                .list(listRef)
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .checked(item.isChecked())
                .build();
        return mapper.itemToDomain(itemRepository.save(entity));
    }

    @Override
    public Optional<ShoppingListItem> findItemByIdAndListId(UUID itemId, UUID listId) {
        return itemRepository.findByIdAndListId(itemId, listId)
                .map(mapper::itemToDomain);
    }
}
