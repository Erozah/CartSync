package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import com.erozah.kestachet.domain.model.ShoppingList;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record ShoppingListResponse(
    String id,
    String title,
    String ownerId,
    Set<String> memberIds,
    List<ItemResponse> items,
    BigDecimal maxBudget,
    BigDecimal totalCart,
    BudgetResponse budget,
    boolean isArchived,
    Instant createdAt
) {
    public static ShoppingListResponse fromDomain(ShoppingList list) {
        return new ShoppingListResponse(
            list.getId(),
            list.getTitle(),
            list.getOwnerId(),
            list.getMemberIds(),
            list.getItems().stream().map(ItemResponse::fromDomain).toList(),
            list.getMaxBudget(),
            list.calculateTotalCart(),
            BudgetResponse.fromDomain(list.computeBudget()),
            list.isArchived(),
            list.getCreatedAt()
        );
    }
}
