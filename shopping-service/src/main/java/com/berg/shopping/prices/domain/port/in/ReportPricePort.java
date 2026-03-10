package com.berg.shopping.prices.domain.port.in;

import com.berg.shopping.prices.application.command.ReportPriceCommand;
import com.berg.shopping.prices.domain.model.ProductPrice;

public interface ReportPricePort {

    ProductPrice execute(ReportPriceCommand command);
}
