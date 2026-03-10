package com.berg.shopping.prices.domain.port.out;

import com.berg.shopping.prices.domain.model.ProductPrice;

import java.util.List;

public interface PriceRepositoryPort {

    ProductPrice save(ProductPrice price);

    List<ProductPrice> findLatestByProductName(String productName);

    List<ProductPrice> findLatestByProductNames(List<String> productNames);
}
