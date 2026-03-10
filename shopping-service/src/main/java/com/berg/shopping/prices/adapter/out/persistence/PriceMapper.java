package com.berg.shopping.prices.adapter.out.persistence;

import com.berg.shopping.prices.domain.model.ProductPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    ProductPrice toDomain(ProductPriceJpaEntity entity);

    ProductPriceJpaEntity toEntity(ProductPrice domain);
}
