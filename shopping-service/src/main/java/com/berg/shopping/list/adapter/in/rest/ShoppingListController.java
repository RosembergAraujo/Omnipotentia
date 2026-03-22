package com.berg.shopping.list.adapter.in.rest;

import com.berg.common.security.JwtAuthenticationToken;
import com.berg.shopping.list.adapter.in.rest.dto.*;
import com.berg.shopping.list.application.command.AddItemCommand;
import com.berg.shopping.list.application.command.CheckItemCommand;
import com.berg.shopping.list.application.command.CreateListCommand;
import com.berg.shopping.list.domain.port.in.*;
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
@RequestMapping("/api/v1/lists")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Shopping Lists", description = "List management endpoints")
public class ShoppingListController {

    private final CreateListPort createListPort;
    private final AddItemToListPort addItemToListPort;
    private final GetListWithItemsPort getListWithItemsPort;
    private final CheckItemPort checkItemPort;

    @PostMapping
    @Operation(summary = "Create a new shopping list")
    public ResponseEntity<ShoppingListResponse> createList(
            @Valid @RequestBody CreateListRequest request,
            JwtAuthenticationToken authentication) {

        var list = createListPort.execute(new CreateListCommand(request.name(), authentication.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListResponse.from(list));
    }

    @GetMapping
    @Operation(summary = "Get all lists for the authenticated user")
    public ResponseEntity<List<ShoppingListResponse>> getAllLists(JwtAuthenticationToken authentication) {
        List<ShoppingListResponse> lists = getListWithItemsPort.findAllByOwner(authentication.getUserId())
                .stream()
                .map(ShoppingListResponse::from)
                .toList();
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{listId}")
    @Operation(summary = "Get a list with all its items")
    public ResponseEntity<ShoppingListResponse> getList(
            @PathVariable UUID listId,
            JwtAuthenticationToken authentication) {

        var list = getListWithItemsPort.execute(listId, authentication.getUserId());
        return ResponseEntity.ok(ShoppingListResponse.from(list));
    }

    @PostMapping("/{listId}/items")
    @Operation(summary = "Add an item to a list")
    public ResponseEntity<ShoppingListItemResponse> addItem(
            @PathVariable UUID listId,
            @Valid @RequestBody AddItemRequest request,
            JwtAuthenticationToken authentication) {

        var item = addItemToListPort.execute(new AddItemCommand(
                listId, authentication.getUserId(), request.name(), request.quantity(), request.unit()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListItemResponse.from(item));
    }

    @PatchMapping("/{listId}/items/{itemId}/check")
    @Operation(summary = "Toggle the checked status of an item")
    public ResponseEntity<ShoppingListItemResponse> checkItem(
            @PathVariable UUID listId,
            @PathVariable UUID itemId,
            JwtAuthenticationToken authentication) {

        var item = checkItemPort.execute(new CheckItemCommand(listId, itemId, authentication.getUserId()));
        return ResponseEntity.ok(ShoppingListItemResponse.from(item));
    }
}
