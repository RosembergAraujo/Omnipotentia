package com.berg.shopping.list.adapter.out.persistence;

import com.berg.shopping.list.domain.model.ShoppingList;
import com.berg.shopping.list.domain.model.ShoppingListItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShoppingListMapper {

    public ShoppingList toDomain(ShoppingListJpaEntity entity) {
        List<ShoppingListItem> items = entity.getItems().stream()
                .map(this::itemToDomain)
                .toList();
        return ShoppingList.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ownerId(entity.getOwnerId())
                .createdAt(entity.getCreatedAt())
                .items(items)
                .build();
    }

    public ShoppingListItem itemToDomain(ShoppingListItemJpaEntity entity) {
        return ShoppingListItem.builder()
                .id(entity.getId())
                .listId(entity.getList().getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .checked(entity.isChecked())
                .addedAt(entity.getAddedAt())
                .build();
    }

    public ShoppingListJpaEntity toEntity(ShoppingList domain) {
        return ShoppingListJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .ownerId(domain.getOwnerId())
                .build();
    }
}
