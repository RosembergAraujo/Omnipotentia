package com.berg.shopping.prices.adapter.out.persistence;

import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final ProductPriceJpaRepository repository;
    private final PriceMapper mapper;

    @Override
    public ProductPrice save(ProductPrice price) {
        return mapper.toDomain(repository.save(mapper.toEntity(price)));
    }

    @Override
    public List<ProductPrice> findLatestByProductName(String productName) {
        return repository.findByProductNameOrderByReportedAtDesc(productName).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ProductPrice> findLatestByProductNames(List<String> productNames) {
        return repository.findByProductNameInOrderByReportedAtDesc(productNames).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
