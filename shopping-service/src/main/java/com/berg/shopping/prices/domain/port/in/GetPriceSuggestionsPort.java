package com.berg.shopping.prices.domain.port.in;

import com.berg.shopping.prices.application.dto.StorePriceSummary;

import java.util.List;
import java.util.UUID;

public interface GetPriceSuggestionsPort {

    List<StorePriceSummary> execute(UUID listId, UUID ownerId);
}
