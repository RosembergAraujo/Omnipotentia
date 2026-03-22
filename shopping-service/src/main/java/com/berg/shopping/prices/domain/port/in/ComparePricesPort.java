package com.berg.shopping.prices.domain.port.in;

import com.berg.shopping.prices.domain.model.ProductPrice;

import java.util.List;

public interface ComparePricesPort {

    List<ProductPrice> execute(String productName);
}
