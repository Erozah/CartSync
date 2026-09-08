package com.erozah.kestachet.infrastructure.adapter.in.web;

import com.erozah.kestachet.domain.model.Budget;
import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.port.in.CalculateBudgetUseCase;
import com.erozah.kestachet.domain.port.in.ManageListUseCase;
import com.erozah.kestachet.domain.port.in.ValidateCartUseCase;
import com.erozah.kestachet.infrastructure.adapter.in.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {

    private final ManageListUseCase manageListUseCase;
    private final CalculateBudgetUseCase calculateBudgetUseCase;
    private final ValidateCartUseCase validateCartUseCase;

    public ShoppingListController(
        ManageListUseCase manageListUseCase,
        CalculateBudgetUseCase calculateBudgetUseCase,
        ValidateCartUseCase validateCartUseCase
    ) {
        this.manageListUseCase = manageListUseCase;
        this.calculateBudgetUseCase = calculateBudgetUseCase;
        this.validateCartUseCase = validateCartUseCase;
    }

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createList(
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId,
            @Valid @RequestBody CreateListRequest request
    ) {
        ShoppingList list = manageListUseCase.createList(request.title(), userId, request.maxBudget());
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListResponse.fromDomain(list));
    }

    @GetMapping
    public ResponseEntity<List<ShoppingListResponse>> getUserLists(
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        List<ShoppingListResponse> lists = manageListUseCase.getUserLists(userId).stream()
                .map(ShoppingListResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListResponse> getList(
            @PathVariable("id") String listId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        ShoppingList list = manageListUseCase.getList(listId, userId);
        return ResponseEntity.ok(ShoppingListResponse.fromDomain(list));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ItemResponse> addItem(
            @PathVariable("id") String listId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId,
            @RequestBody AddItemRequest request
    ) {
        int qty = request.quantity() != null ? request.quantity() : 1;
        Item item = manageListUseCase.addItemToList(
            listId, userId, request.gtin(), request.name(), qty, request.unit(), request.price()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponse.fromDomain(item));
    }

    @PatchMapping("/{id}/items/{itemId}/toggle")
    public ResponseEntity<Void> toggleItemBought(
            @PathVariable("id") String listId,
            @PathVariable("itemId") String itemId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        manageListUseCase.toggleItemBought(listId, itemId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> updateItem(
            @PathVariable("id") String listId,
            @PathVariable("itemId") String itemId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId,
            @RequestBody UpdateItemRequest request
    ) {
        manageListUseCase.updateItem(listId, itemId, userId, request.quantity(), request.price());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable("id") String listId,
            @PathVariable("itemId") String itemId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        manageListUseCase.removeItem(listId, itemId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> shareList(
            @PathVariable("id") String listId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId,
            @Valid @RequestBody ShareListRequest request
    ) {
        manageListUseCase.shareList(listId, userId, request.targetUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/budget")
    public ResponseEntity<BudgetResponse> getBudget(
            @PathVariable("id") String listId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        Budget budget = calculateBudgetUseCase.calculateBudget(listId, userId);
        return ResponseEntity.ok(BudgetResponse.fromDomain(budget));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validateCart(
            @PathVariable("id") String listId,
            @RequestHeader(value = "X-User-Id", defaultValue = "user-default") String userId
    ) {
        validateCartUseCase.validateAndArchiveCart(listId, userId);
        return ResponseEntity.noContent().build();
    }
}
