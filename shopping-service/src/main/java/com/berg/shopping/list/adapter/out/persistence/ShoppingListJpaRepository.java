package com.berg.shopping.list.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListJpaRepository extends JpaRepository<ShoppingListJpaEntity, UUID> {

    Optional<ShoppingListJpaEntity> findByIdAndOwnerId(UUID id, UUID ownerId);

    List<ShoppingListJpaEntity> findAllByOwnerId(UUID ownerId);
}
