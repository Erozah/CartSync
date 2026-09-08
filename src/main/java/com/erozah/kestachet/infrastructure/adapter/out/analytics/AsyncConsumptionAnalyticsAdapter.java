package com.erozah.kestachet.infrastructure.adapter.out.analytics;

import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.port.out.ConsumptionAnalyticsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncConsumptionAnalyticsAdapter implements ConsumptionAnalyticsPort {

    private static final Logger log = LoggerFactory.getLogger(AsyncConsumptionAnalyticsAdapter.class);

    @Override
    @Async
    public void publishValidatedCart(ShoppingList validatedList) {
        log.info("[A Propos Des Biens] Réception du panier validé: listId={}, totalSpent={} €, itemsCount={}",
                validatedList.getId(),
                validatedList.calculateTotalCart(),
                validatedList.getItems().size());

        for (Item item : validatedList.getItems()) {
            if (item.isBought()) {
                log.debug("[A Propos Des Biens] Article consommé: gtin={}, name={}, qty={}",
                        item.getGtin(), item.getName(), item.getQuantity());
            }
        }
    }
}
