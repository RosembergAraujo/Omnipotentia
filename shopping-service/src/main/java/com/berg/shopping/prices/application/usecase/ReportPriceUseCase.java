package com.berg.shopping.prices.application.usecase;

import com.berg.shopping.prices.application.command.ReportPriceCommand;
import com.berg.shopping.prices.domain.model.ProductPrice;
import com.berg.shopping.prices.domain.port.in.ReportPricePort;
import com.berg.shopping.prices.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPriceUseCase implements ReportPricePort {

    private final PriceRepositoryPort repository;

    @Override
    @Transactional
    public ProductPrice execute(ReportPriceCommand command) {
        ProductPrice price = ProductPrice.create(
                command.productName(),
                command.storeName(),
                command.price(),
                command.reportedBy()
        );
        return repository.save(price);
    }
}
