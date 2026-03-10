package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.in.ComparePricesPort;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComparePricesUseCase implements ComparePricesPort {

    private final PriceRepositoryPort repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductPrice> execute(String productName) {
        List<ProductPrice> all = repository.findLatestByProductName(productName.toLowerCase().trim());
        return latestPerStoreSortedByPrice(all);
    }

    private List<ProductPrice> latestPerStoreSortedByPrice(List<ProductPrice> prices) {
        Map<String, ProductPrice> latestByStore = new LinkedHashMap<>();
        for (ProductPrice price : prices) {
            latestByStore.putIfAbsent(price.getStoreName(), price);
        }
        return latestByStore.values().stream()
                .sorted((a, b) -> a.getPrice().compareTo(b.getPrice()))
                .toList();
    }
}
