package com.erozah.kestachet.domain.port.in;

import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;

import java.math.BigDecimal;
import java.util.List;

public interface ManageListUseCase {
    ShoppingList createList(String title, String ownerId, BigDecimal maxBudget);
    ShoppingList getList(String listId, String userId);
    List<ShoppingList> getUserLists(String userId);
    void shareList(String listId, String ownerId, String targetUserId);
    Item addItemToList(String listId, String userId, String gtin, String name, int quantity, String unit, BigDecimal price);
    void toggleItemBought(String listId, String itemId, String userId);
    void updateItem(String listId, String itemId, String userId, int quantity, BigDecimal price);
    void removeItem(String listId, String itemId, String userId);
}
