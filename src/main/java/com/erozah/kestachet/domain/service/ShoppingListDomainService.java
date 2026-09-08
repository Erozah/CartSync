package com.erozah.kestachet.domain.service;

import com.erozah.kestachet.domain.model.Budget;
import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.Product;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.port.in.CalculateBudgetUseCase;
import com.erozah.kestachet.domain.port.in.ManageListUseCase;
import com.erozah.kestachet.domain.port.in.ValidateCartUseCase;
import com.erozah.kestachet.domain.port.out.ConsumptionAnalyticsPort;
import com.erozah.kestachet.domain.port.out.ExternalCatalogPort;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ShoppingListDomainService implements ManageListUseCase, CalculateBudgetUseCase, ValidateCartUseCase {

    private final ShoppingListRepositoryPort repositoryPort;
    private final ExternalCatalogPort catalogPort;
    private final ConsumptionAnalyticsPort analyticsPort;
    private final BudgetEngine budgetEngine;

    public ShoppingListDomainService(
        ShoppingListRepositoryPort repositoryPort,
        ExternalCatalogPort catalogPort,
        ConsumptionAnalyticsPort analyticsPort,
        BudgetEngine budgetEngine
    ) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort);
        this.catalogPort = Objects.requireNonNull(catalogPort);
        this.analyticsPort = Objects.requireNonNull(analyticsPort);
        this.budgetEngine = Objects.requireNonNull(budgetEngine);
    }

    @Override
    public ShoppingList createList(String title, String ownerId, BigDecimal maxBudget) {
        String listId = UUID.randomUUID().toString();
        ShoppingList list = new ShoppingList(listId, title, ownerId, maxBudget);
        return repositoryPort.save(list);
    }

    @Override
    public ShoppingList getList(String listId, String userId) {
        ShoppingList list = repositoryPort.findById(listId)
            .orElseThrow(() -> new IllegalArgumentException("Liste introuvable"));
        if (!list.isAccessibleBy(userId)) {
            throw new SecurityException("Accès non autorisé à cette liste");
        }
        return list;
    }

    @Override
    public List<ShoppingList> getUserLists(String userId) {
        return repositoryPort.findAllByUserId(userId);
    }

    @Override
    public void shareList(String listId, String ownerId, String targetUserId) {
        ShoppingList list = getList(listId, ownerId);
        if (!list.getOwnerId().equals(ownerId)) {
            throw new SecurityException("Seul le propriétaire peut partager la liste");
        }
        list.addMember(targetUserId);
        repositoryPort.save(list);
    }

    @Override
    public Item addItemToList(String listId, String userId, String gtin, String name, int quantity, String unit, BigDecimal price) {
        ShoppingList list = getList(listId, userId);
        
        String finalName = name;
        BigDecimal finalPrice = price;

        if (gtin != null && !gtin.isBlank()) {
            Optional<Product> productOpt = catalogPort.findByGtin(gtin);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                if (finalName == null || finalName.isBlank()) {
                    finalName = product.name();
                }
                if (finalPrice == null || finalPrice.compareTo(BigDecimal.ZERO) == 0) {
                    finalPrice = product.estimatedPrice();
                }
            }
        }

        if (finalName == null || finalName.isBlank()) {
            finalName = "Article";
        }

        String itemId = UUID.randomUUID().toString();
        Item item = new Item(itemId, gtin, finalName, quantity, unit, finalPrice, false, null);
        list.addItem(item);
        repositoryPort.save(list);
        return item;
    }

    @Override
    public void toggleItemBought(String listId, String itemId, String userId) {
        ShoppingList list = getList(listId, userId);
        Item item = list.findItemById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));
        item.toggleBought(userId);
        repositoryPort.save(list);
    }

    @Override
    public void updateItem(String listId, String itemId, String userId, int quantity, BigDecimal price) {
        ShoppingList list = getList(listId, userId);
        Item item = list.findItemById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));
        item.setQuantity(quantity);
        item.setUnitPrice(price);
        repositoryPort.save(list);
    }

    @Override
    public void removeItem(String listId, String itemId, String userId) {
        ShoppingList list = getList(listId, userId);
        list.removeItem(itemId);
        repositoryPort.save(list);
    }

    @Override
    public Budget calculateBudget(String listId, String userId) {
        ShoppingList list = getList(listId, userId);
        return budgetEngine.calculateBudget(list);
    }

    @Override
    public void validateAndArchiveCart(String listId, String userId) {
        ShoppingList list = getList(listId, userId);
        list.setArchived(true);
        repositoryPort.save(list);
        analyticsPort.publishValidatedCart(list);
    }
}
