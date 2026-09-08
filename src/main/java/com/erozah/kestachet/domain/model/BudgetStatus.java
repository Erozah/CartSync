package com.erozah.kestachet.domain.model;

public enum BudgetStatus {
    GREEN,   // < 80% du budget
    ORANGE,  // 80% à 99% du budget
    RED      // >= 100% (budget atteint ou dépassé)
}
