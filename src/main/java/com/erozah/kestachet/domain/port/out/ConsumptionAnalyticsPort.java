package com.erozah.kestachet.domain.port.out;

import com.erozah.kestachet.domain.model.ShoppingList;

public interface ConsumptionAnalyticsPort {
    void publishValidatedCart(ShoppingList validatedList);
}
