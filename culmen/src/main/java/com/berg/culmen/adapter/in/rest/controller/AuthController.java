package com.berg.culmen.adapter.in.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.berg.culmen.adapter.in.rest.dto.AuthRequest;
import com.berg.culmen.application.command.GenerateTokenCommand;
import com.berg.culmen.application.dto.AuthTokenResponse;
import com.berg.culmen.domain.port.in.GenerateTokenPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final GenerateTokenPort generateTokenPort;

    @PostMapping("/token")
    @Operation(summary = "Generate JWT token for a Telegram user")
    public ResponseEntity<AuthTokenResponse> generateToken(@Valid @RequestBody AuthRequest request) {
        AuthTokenResponse response = generateTokenPort.execute(
                new GenerateTokenCommand(request.telegramId(), request.telegramUsername()));
        return ResponseEntity.ok(response);
    }
}
