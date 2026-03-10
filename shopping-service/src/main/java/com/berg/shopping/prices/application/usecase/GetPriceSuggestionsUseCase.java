package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.application.dto.ItemPriceDetail;
import com.berg.shopping.prices.application.dto.StorePriceSummary;
import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.in.GetPriceSuggestionsPort;
import com.berg.shopping.prices.domain.port.out.ListItemQueryPort;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GetPriceSuggestionsUseCase implements GetPriceSuggestionsPort {

    private final ListItemQueryPort listItemQueryPort;
    private final PriceRepositoryPort priceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<StorePriceSummary> execute(UUID listId, UUID ownerId) {
        List<String> productNames = listItemQueryPort.findByListAndOwner(listId, ownerId).stream()
                .filter(item -> !item.checked())
                .map(item -> item.name().toLowerCase().trim())
                .distinct()
                .toList();

        if (productNames.isEmpty()) {
            return List.of();
        }

        List<ProductPrice> prices = priceRepository.findLatestByProductNames(productNames);

        // For each (productName, storeName), keep only the most recent price
        Map<String, Map<String, ProductPrice>> latestByProductAndStore = new LinkedHashMap<>();
        for (ProductPrice price : prices) {
            latestByProductAndStore
                    .computeIfAbsent(price.getProductName(), k -> new LinkedHashMap<>())
                    .putIfAbsent(price.getStoreName(), price);
        }

        // Group by store: collect cheapest price per product at that store
        Map<String, List<ItemPriceDetail>> itemsByStore = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, ProductPrice>> productEntry : latestByProductAndStore.entrySet()) {
            ProductPrice cheapest = productEntry.getValue().values().stream()
                    .min(Comparator.comparing(ProductPrice::getPrice))
                    .orElseThrow();

            itemsByStore
                    .computeIfAbsent(cheapest.getStoreName(), k -> new ArrayList<>())
                    .add(new ItemPriceDetail(productEntry.getKey(), cheapest.getPrice()));
        }

        return itemsByStore.entrySet().stream()
                .map(entry -> {
                    BigDecimal total = entry.getValue().stream()
                            .map(ItemPriceDetail::price)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new StorePriceSummary(entry.getKey(), entry.getValue(), total);
                })
                .sorted(Comparator.comparing(StorePriceSummary::estimatedTotal).reversed())
                .toList();
    }
}
