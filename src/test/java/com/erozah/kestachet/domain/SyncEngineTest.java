package com.erozah.kestachet.domain;

import com.erozah.kestachet.domain.model.OperationType;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.model.SyncOperation;
import com.erozah.kestachet.domain.port.in.SyncBatchUseCase.SyncBatchResult;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;
import com.erozah.kestachet.domain.port.out.SyncJournalPort;
import com.erozah.kestachet.domain.service.SyncEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SyncEngineTest {

    private SyncEngine syncEngine;
    private InMemoryShoppingListRepository repository;
    private InMemorySyncJournal journal;

    @BeforeEach
    void setUp() {
        repository = new InMemoryShoppingListRepository();
        journal = new InMemorySyncJournal();
        syncEngine = new SyncEngine(repository, journal);
    }

    @Test
    @DisplayName("Doit appliquer les opérations du batch et ignorer les doublons (idempotence)")
    void shouldProcessBatchAndHandleIdempotency() {
        ShoppingList list = new ShoppingList("list-1", "Maison", "user-1", BigDecimal.valueOf(50.00));
        repository.save(list);

        SyncOperation op1 = new SyncOperation(
            "op-101", "list-1", "user-1", OperationType.ADD_ITEM,
            "item-1", "3017620422003", "Nutella", 2, BigDecimal.valueOf(4.50), Instant.now()
        );

        SyncOperation op2 = new SyncOperation(
            "op-102", "list-1", "user-1", OperationType.TOGGLE_BOUGHT,
            "item-1", null, null, null, null, Instant.now()
        );

        // 1er envoi du batch
        SyncBatchResult result1 = syncEngine.processBatch(List.of(op1, op2));

        assertEquals(2, result1.appliedCount());
        assertEquals(0, result1.duplicateCount());
        assertTrue(result1.errors().isEmpty());

        ShoppingList updatedList = repository.findById("list-1").orElseThrow();
        assertEquals(1, updatedList.getItems().size());
        assertTrue(updatedList.getItems().get(0).isBought());

        // 2eme envoi du même batch (simulation de retry réseau)
        SyncBatchResult result2 = syncEngine.processBatch(List.of(op1, op2));

        assertEquals(0, result2.appliedCount());
        assertEquals(2, result2.duplicateCount()); // 2 doublons ignorés
        assertEquals(1, updatedList.getItems().size()); // pas d'article dupliqué
    }

    // Faux adaptateurs de test (In-Memory)
    private static class InMemoryShoppingListRepository implements ShoppingListRepositoryPort {
        private final Map<String, ShoppingList> storage = new HashMap<>();

        @Override
        public ShoppingList save(ShoppingList shoppingList) {
            storage.put(shoppingList.getId(), shoppingList);
            return shoppingList;
        }

        @Override
        public Optional<ShoppingList> findById(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<ShoppingList> findAllByUserId(String userId) {
            return storage.values().stream().filter(l -> l.isAccessibleBy(userId)).toList();
        }

        @Override
        public void deleteById(String id) {
            storage.remove(id);
        }
    }

    private static class InMemorySyncJournal implements SyncJournalPort {
        private final Set<String> processed = new HashSet<>();

        @Override
        public boolean isOperationAlreadyProcessed(String operationId) {
            return processed.contains(operationId);
        }

        @Override
        public void recordProcessedOperation(String operationId, String listId, String userId) {
            processed.add(operationId);
        }
    }
}
