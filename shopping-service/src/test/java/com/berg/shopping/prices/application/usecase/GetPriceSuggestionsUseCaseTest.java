package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.application.dto.StorePriceSummary;
import com.berg.shopping.prices.domain.model.ListItemView;
import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.out.ListItemQueryPort;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceSuggestionsUseCaseTest {

    @Mock
    private ListItemQueryPort listItemQueryPort;

    @Mock
    private PriceRepositoryPort priceRepository;

    @InjectMocks
    private GetPriceSuggestionsUseCase useCase;

    @Test
    void shouldReturnEmptyWhenNoUncheckedItems() {
        UUID listId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        when(listItemQueryPort.findByListAndOwner(listId, ownerId)).thenReturn(List.of(
                new ListItemView(UUID.randomUUID(), "Arroz", BigDecimal.ONE, true)
        ));

        List<StorePriceSummary> result = useCase.execute(listId, ownerId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldGroupCheapestPricesByStore() {
        UUID listId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID user = UUID.randomUUID();

        when(listItemQueryPort.findByListAndOwner(listId, ownerId)).thenReturn(List.of(
                new ListItemView(UUID.randomUUID(), "Arroz", BigDecimal.ONE, false),
                new ListItemView(UUID.randomUUID(), "Feijão", BigDecimal.ONE, false)
        ));

        ProductPrice arroz_lojaA  = ProductPrice.builder().id(UUID.randomUUID()).productName("arroz").storeName("Loja A").price(new BigDecimal("5.00")).reportedBy(user).build();
        ProductPrice feijao_lojaA = ProductPrice.builder().id(UUID.randomUUID()).productName("feijão").storeName("Loja A").price(new BigDecimal("8.00")).reportedBy(user).build();
        ProductPrice arroz_lojaB  = ProductPrice.builder().id(UUID.randomUUID()).productName("arroz").storeName("Loja B").price(new BigDecimal("4.00")).reportedBy(user).build();

        when(priceRepository.findLatestByProductNames(anyList())).thenReturn(List.of(arroz_lojaA, feijao_lojaA, arroz_lojaB));

        List<StorePriceSummary> result = useCase.execute(listId, ownerId);

        // Loja B has cheapest arroz, Loja A has cheapest feijao
        assertThat(result).hasSize(2);
        StorePriceSummary lojaA = result.stream().filter(s -> s.storeName().equals("Loja A")).findFirst().orElseThrow();
        StorePriceSummary lojaB = result.stream().filter(s -> s.storeName().equals("Loja B")).findFirst().orElseThrow();

        assertThat(lojaA.estimatedTotal()).isEqualByComparingTo("8.00");
        assertThat(lojaB.estimatedTotal()).isEqualByComparingTo("4.00");
    }
}
