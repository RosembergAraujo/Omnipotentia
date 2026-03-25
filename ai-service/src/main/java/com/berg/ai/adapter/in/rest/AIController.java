package com.berg.ai.adapter.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.berg.ai.adapter.in.rest.dto.ParseMessageRequest;
import com.berg.ai.adapter.in.rest.dto.ParseMessageResponse;
import com.berg.ai.domain.port.in.ParseMessagePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Natural language message parsing")
@SecurityRequirement(name = "bearerAuth")
public class AIController {

    private final ParseMessagePort parseMessagePort;

    @PostMapping("/parse")
    @Operation(summary = "Parse a natural language message and extract structured intent and data")
    public ResponseEntity<ParseMessageResponse> parse(@Valid @RequestBody ParseMessageRequest request) {
        return ResponseEntity.ok(
                ParseMessageResponse.from(parseMessagePort.execute(request.message())));
    }
}
