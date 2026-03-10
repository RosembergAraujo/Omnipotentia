package com.berg.shopping.list.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ShoppingList {

    private UUID id;
    private String name;
    private UUID ownerId;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<ShoppingListItem> items = List.of();

    public static ShoppingList create(String name, UUID ownerId) {
        return ShoppingList.builder()
                .name(name)
                .ownerId(ownerId)
                .build();
    }
}
