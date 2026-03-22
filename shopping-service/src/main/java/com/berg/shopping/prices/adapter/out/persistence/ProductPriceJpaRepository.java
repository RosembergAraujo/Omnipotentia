package com.berg.shopping.prices.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductPriceJpaRepository extends JpaRepository<ProductPriceJpaEntity, UUID> {

    List<ProductPriceJpaEntity> findByProductNameOrderByReportedAtDesc(String productName);

    @Query("SELECT p FROM ProductPriceJpaEntity p WHERE p.productName IN :names ORDER BY p.productName, p.reportedAt DESC")
    List<ProductPriceJpaEntity> findByProductNameInOrderByReportedAtDesc(@Param("names") List<String> names);
}
