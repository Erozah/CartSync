package com.erozah.kestachet.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Budget(
    BigDecimal maxAmount,
    BigDecimal currentSpent,
    BigDecimal remainingAmount,
    double percentageUsed,
    BudgetStatus status
) {
    public static Budget of(BigDecimal maxAmount, BigDecimal currentSpent) {
        BigDecimal max = maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) > 0 
            ? maxAmount 
            : BigDecimal.ZERO;
        BigDecimal spent = currentSpent != null ? currentSpent : BigDecimal.ZERO;

        BigDecimal remaining = max.subtract(spent);
        double percentage = 0.0;
        
        if (max.compareTo(BigDecimal.ZERO) > 0) {
            percentage = spent.divide(max, 4, RoundingMode.HALF_UP)
                              .multiply(BigDecimal.valueOf(100))
                              .doubleValue();
        }

        BudgetStatus status;
        if (percentage < 80.0) {
            status = BudgetStatus.GREEN;
        } else if (percentage < 100.0) {
            status = BudgetStatus.ORANGE;
        } else {
            status = BudgetStatus.RED;
        }

        return new Budget(max, spent, remaining, percentage, status);
    }
}
