package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComparePricesUseCaseTest {

    @Mock
    private PriceRepositoryPort repository;

    @InjectMocks
    private ComparePricesUseCase useCase;

    @Test
    void shouldReturnLatestPricePerStoreSortedAscending() {
        UUID user = UUID.randomUUID();
        LocalDateTime older = LocalDateTime.now().minusDays(2);
        LocalDateTime newer = LocalDateTime.now().minusDays(1);

        ProductPrice storeA_old = ProductPrice.builder().id(UUID.randomUUID()).productName("arroz").storeName("Loja A").price(new BigDecimal("6.00")).reportedBy(user).reportedAt(older).build();
        ProductPrice storeA_new = ProductPrice.builder().id(UUID.randomUUID()).productName("arroz").storeName("Loja A").price(new BigDecimal("5.50")).reportedBy(user).reportedAt(newer).build();
        ProductPrice storeB     = ProductPrice.builder().id(UUID.randomUUID()).productName("arroz").storeName("Loja B").price(new BigDecimal("4.80")).reportedBy(user).reportedAt(newer).build();

        // repository returns newest first (sorted by reportedAt DESC)
        when(repository.findLatestByProductName("arroz")).thenReturn(List.of(storeA_new, storeA_old, storeB));

        List<ProductPrice> result = useCase.execute("arroz");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStoreName()).isEqualTo("Loja B");
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("4.80");
        assertThat(result.get(1).getStoreName()).isEqualTo("Loja A");
        assertThat(result.get(1).getPrice()).isEqualByComparingTo("5.50");
    }
}
