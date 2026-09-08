package com.erozah.kestachet.domain;

import com.erozah.kestachet.domain.model.Budget;
import com.erozah.kestachet.domain.model.BudgetStatus;
import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.service.BudgetEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BudgetEngineTest {

    private BudgetEngine budgetEngine;

    @BeforeEach
    void setUp() {
        budgetEngine = new BudgetEngine();
    }

    @Test
    @DisplayName("Le budget doit être VERT lorsque les dépenses sont inférieures à 80%")
    void shouldReturnGreenWhenUnder80Percent() {
        ShoppingList list = new ShoppingList("list-1", "Courses", "user-1", BigDecimal.valueOf(100.00));
        list.addItem(new Item("item-1", null, "Pâtes", 2, "paquet", BigDecimal.valueOf(25.00), false, null)); // 50.00€

        Budget budget = budgetEngine.calculateBudget(list);

        assertEquals(BudgetStatus.GREEN, budget.status());
        assertEquals(0, BigDecimal.valueOf(100.00).compareTo(budget.maxAmount()));
        assertEquals(0, BigDecimal.valueOf(50.00).compareTo(budget.currentSpent()));
        assertEquals(0, BigDecimal.valueOf(50.00).compareTo(budget.remainingAmount()));
        assertEquals(50.0, budget.percentageUsed());
    }

    @Test
    @DisplayName("Le budget doit être ORANGE lorsque les dépenses sont entre 80% et 99%")
    void shouldReturnOrangeWhenBetween80And99Percent() {
        ShoppingList list = new ShoppingList("list-1", "Courses", "user-1", BigDecimal.valueOf(100.00));
        list.addItem(new Item("item-1", null, "Viande", 1, "kg", BigDecimal.valueOf(85.00), false, null)); // 85.00€

        Budget budget = budgetEngine.calculateBudget(list);

        assertEquals(BudgetStatus.ORANGE, budget.status());
        assertEquals(0, BigDecimal.valueOf(15.00).compareTo(budget.remainingAmount()));
        assertEquals(85.0, budget.percentageUsed());
    }

    @Test
    @DisplayName("Le budget doit être ROUGE lorsque les dépenses atteignent ou dépassent 100%")
    void shouldReturnRedWhenAtOrOver100Percent() {
        ShoppingList list = new ShoppingList("list-1", "Courses", "user-1", BigDecimal.valueOf(100.00));
        list.addItem(new Item("item-1", null, "Articles divers", 1, "lot", BigDecimal.valueOf(110.00), false, null)); // 110.00€

        Budget budget = budgetEngine.calculateBudget(list);

        assertEquals(BudgetStatus.RED, budget.status());
        assertEquals(0, BigDecimal.valueOf(-10.00).compareTo(budget.remainingAmount()));
        assertEquals(110.0, budget.percentageUsed());
    }
}
