package com.erozah.kestachet.domain.service;

import com.erozah.kestachet.domain.model.Budget;
import com.erozah.kestachet.domain.model.ShoppingList;

import java.math.BigDecimal;
import java.util.Objects;

public class BudgetEngine {

    public Budget calculateBudget(ShoppingList list) {
        Objects.requireNonNull(list, "La liste de courses ne peut pas être nulle");
        BigDecimal totalSpent = list.calculateTotalCart();
        return Budget.of(list.getMaxBudget(), totalSpent);
    }
}
