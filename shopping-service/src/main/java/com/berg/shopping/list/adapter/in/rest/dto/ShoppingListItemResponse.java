package com.berg.shopping.list.adapter.in.rest.dto;

import com.berg.shopping.list.domain.model.ShoppingListItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShoppingListItemResponse(
        UUID id,
        UUID listId,
        String name,
        BigDecimal quantity,
        String unit,
        boolean checked,
        LocalDateTime addedAt
) {
    public static ShoppingListItemResponse from(ShoppingListItem item) {
        return new ShoppingListItemResponse(
                item.getId(),
                item.getListId(),
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.isChecked(),
                item.getAddedAt()
        );
    }
}
