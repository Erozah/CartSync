package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import com.erozah.kestachet.domain.model.Budget;
import com.erozah.kestachet.domain.model.BudgetStatus;
import java.math.BigDecimal;

public record BudgetResponse(
    BigDecimal maxAmount,
    BigDecimal currentSpent,
    BigDecimal remainingAmount,
    double percentageUsed,
    BudgetStatus status
) {
    public static BudgetResponse fromDomain(Budget budget) {
        return new BudgetResponse(
            budget.maxAmount(),
            budget.currentSpent(),
            budget.remainingAmount(),
            budget.percentageUsed(),
            budget.status()
        );
    }
}
