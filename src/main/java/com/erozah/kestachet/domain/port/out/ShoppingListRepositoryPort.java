package com.erozah.kestachet.domain.port.out;

import com.erozah.kestachet.domain.model.ShoppingList;
import java.util.List;
import java.util.Optional;

public interface ShoppingListRepositoryPort {
    ShoppingList save(ShoppingList shoppingList);
    Optional<ShoppingList> findById(String id);
    List<ShoppingList> findAllByUserId(String userId);
    void deleteById(String id);
}
