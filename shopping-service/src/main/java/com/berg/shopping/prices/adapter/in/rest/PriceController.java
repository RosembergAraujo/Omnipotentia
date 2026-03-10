package com.berg.shopping.prices.adapter.in.rest;

import com.berg.common.security.JwtAuthenticationToken;
import com.berg.shopping.prices.adapter.in.rest.dto.PriceSuggestionResponse;
import com.berg.shopping.prices.adapter.in.rest.dto.ProductPriceResponse;
import com.berg.shopping.prices.adapter.in.rest.dto.ReportPriceRequest;
import com.berg.shopping.prices.application.command.ReportPriceCommand;
import com.berg.shopping.prices.domain.port.in.ComparePricesPort;
import com.berg.shopping.prices.domain.port.in.GetPriceSuggestionsPort;
import com.berg.shopping.prices.domain.port.in.ReportPricePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Prices", description = "Price reporting and comparison endpoints")
public class PriceController {

    private final ReportPricePort reportPricePort;
    private final ComparePricesPort comparePricesPort;
    private final GetPriceSuggestionsPort getPriceSuggestionsPort;

    @PostMapping
    @Operation(summary = "Report a price for a product at a store")
    public ResponseEntity<ProductPriceResponse> reportPrice(
            @Valid @RequestBody ReportPriceRequest request,
            JwtAuthenticationToken authentication) {

        var price = reportPricePort.execute(new ReportPriceCommand(
                request.productName(),
                request.storeName(),
                request.price(),
                authentication.getUserId()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductPriceResponse.from(price));
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare prices for a product across stores")
    public ResponseEntity<List<ProductPriceResponse>> comparePrices(@RequestParam String productName) {
        List<ProductPriceResponse> prices = comparePricesPort.execute(productName)
                .stream()
                .map(ProductPriceResponse::from)
                .toList();
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/suggestions/{listId}")
    @Operation(summary = "Get store suggestions for cheapest overall shopping for a list")
    public ResponseEntity<List<PriceSuggestionResponse>> getSuggestions(
            @PathVariable UUID listId,
            JwtAuthenticationToken authentication) {

        List<PriceSuggestionResponse> suggestions = getPriceSuggestionsPort
                .execute(listId, authentication.getUserId())
                .stream()
                .map(PriceSuggestionResponse::from)
                .toList();
        return ResponseEntity.ok(suggestions);
    }
}
