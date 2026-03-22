package com.berg.shopping.list.adapter.in.rest.dto;

import com.berg.shopping.list.domain.model.ShoppingList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ShoppingListResponse(
        UUID id,
        String name,
        UUID ownerId,
        LocalDateTime createdAt,
        List<ShoppingListItemResponse> items
) {
    public static ShoppingListResponse from(ShoppingList list) {
        List<ShoppingListItemResponse> items = list.getItems().stream()
                .map(ShoppingListItemResponse::from)
                .toList();
        return new ShoppingListResponse(
                list.getId(), list.getName(), list.getOwnerId(), list.getCreatedAt(), items);
    }
}
