package com.berg.culmen.adapter.in.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.berg.culmen.adapter.in.rest.dto.UserRequest;
import com.berg.culmen.adapter.in.rest.dto.UserResponse;
import com.berg.culmen.application.command.CreateUserCommand;
import com.berg.culmen.domain.model.User;
import com.berg.culmen.domain.port.in.CreateOrGetUserPort;
import com.berg.culmen.domain.port.in.GetUserByTelegramIdPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final CreateOrGetUserPort createOrGetUserPort;
    private final GetUserByTelegramIdPort getUserByTelegramIdPort;

    @PostMapping
    @Operation(summary = "Create or retrieve user by telegramId (upsert)")
    public ResponseEntity<UserResponse> createOrGet(@Valid @RequestBody UserRequest request) {
        User user = createOrGetUserPort.execute(new CreateUserCommand(
                request.telegramId(),
                request.telegramUsername(),
                request.phoneNumber(),
                request.displayName()));
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/telegram/{telegramId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user by Telegram ID")
    public ResponseEntity<UserResponse> getByTelegramId(@PathVariable String telegramId) {
        User user = getUserByTelegramIdPort.execute(telegramId);
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
