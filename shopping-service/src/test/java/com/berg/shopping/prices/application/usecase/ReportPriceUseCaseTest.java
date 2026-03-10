package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.application.command.ReportPriceCommand;
import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportPriceUseCaseTest {

    @Mock
    private PriceRepositoryPort repository;

    @InjectMocks
    private ReportPriceUseCase useCase;

    @Test
    void shouldNormalizeProductNameAndSavePrice() {
        UUID reportedBy = UUID.randomUUID();
        ProductPrice saved = ProductPrice.builder()
                .id(UUID.randomUUID())
                .productName("arroz branco")
                .storeName("Supermercado X")
                .price(new BigDecimal("5.99"))
                .reportedBy(reportedBy)
                .build();

        when(repository.save(any(ProductPrice.class))).thenReturn(saved);

        ProductPrice result = useCase.execute(
                new ReportPriceCommand("  Arroz Branco  ", "Supermercado X", new BigDecimal("5.99"), reportedBy));

        assertThat(result.getProductName()).isEqualTo("arroz branco");
        assertThat(result.getPrice()).isEqualByComparingTo("5.99");
        verify(repository).save(any(ProductPrice.class));
    }
}
