package com.berg.shopping.list.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItemJpaEntity, UUID> {

    Optional<ShoppingListItemJpaEntity> findByIdAndListId(UUID id, UUID listId);
}
