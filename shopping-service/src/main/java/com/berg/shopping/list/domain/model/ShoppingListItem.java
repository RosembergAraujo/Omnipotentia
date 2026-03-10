package com.berg.shopping.list.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ShoppingListItem {

    private UUID id;
    private UUID listId;
    private String name;

    @Builder.Default
    private BigDecimal quantity = BigDecimal.ONE;

    private String unit;

    @Builder.Default
    private boolean checked = false;

    private LocalDateTime addedAt;

    public static ShoppingListItem create(UUID listId, String name, BigDecimal quantity, String unit) {
        return ShoppingListItem.builder()
                .listId(listId)
                .name(name)
                .quantity(quantity != null ? quantity : BigDecimal.ONE)
                .unit(unit)
                .build();
    }

    public ShoppingListItem toggleChecked() {
        return ShoppingListItem.builder()
                .id(this.id)
                .listId(this.listId)
                .name(this.name)
                .quantity(this.quantity)
                .unit(this.unit)
                .checked(!this.checked)
                .addedAt(this.addedAt)
                .build();
    }
}
