package com.erozah.kestachet.domain.port.in;

import com.erozah.kestachet.domain.model.Budget;

public interface CalculateBudgetUseCase {
    Budget calculateBudget(String listId, String userId);
}
