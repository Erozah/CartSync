package com.erozah.kestachet.domain.service;

import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.model.SyncOperation;
import com.erozah.kestachet.domain.port.in.SyncBatchUseCase;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;
import com.erozah.kestachet.domain.port.out.SyncJournalPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SyncEngine implements SyncBatchUseCase {

    private final ShoppingListRepositoryPort repositoryPort;
    private final SyncJournalPort journalPort;

    public SyncEngine(ShoppingListRepositoryPort repositoryPort, SyncJournalPort journalPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort);
        this.journalPort = Objects.requireNonNull(journalPort);
    }

    @Override
    public SyncBatchResult processBatch(List<SyncOperation> operations) {
        if (operations == null || operations.isEmpty()) {
            return new SyncBatchResult(0, 0, List.of());
        }

        int applied = 0;
        int duplicates = 0;
        List<String> errors = new ArrayList<>();

        for (SyncOperation op : operations) {
            try {
                if (journalPort.isOperationAlreadyProcessed(op.operationId())) {
                    duplicates++;
                    continue;
                }

                Optional<ShoppingList> optionalList = repositoryPort.findById(op.listId());
                if (optionalList.isEmpty()) {
                    errors.add("Liste introuvable pour l'opération : " + op.operationId());
                    continue;
                }

                ShoppingList list = optionalList.get();
                if (!list.isAccessibleBy(op.userId())) {
                    errors.add("Accès refusé pour l'utilisateur sur la liste : " + op.listId());
                    continue;
                }

                applyOperation(list, op);
                repositoryPort.save(list);
                journalPort.recordProcessedOperation(op.operationId(), op.listId(), op.userId());
                applied++;

            } catch (Exception e) {
                errors.add("Erreur sur l'opération " + op.operationId() + " : " + e.getMessage());
            }
        }

        return new SyncBatchResult(applied, duplicates, errors);
    }

    private void applyOperation(ShoppingList list, SyncOperation op) {
        switch (op.type()) {
            case ADD_ITEM -> {
                String itemId = op.itemId() != null ? op.itemId() : java.util.UUID.randomUUID().toString();
                int qty = op.quantity() != null ? op.quantity() : 1;
                Item item = new Item(itemId, op.gtin(), op.itemName(), qty, "unité", op.price(), false, null);
                list.addItem(item);
            }
            case REMOVE_ITEM -> {
                if (op.itemId() != null) {
                    list.removeItem(op.itemId());
                }
            }
            case UPDATE_QUANTITY -> {
                if (op.itemId() != null && op.quantity() != null) {
                    list.findItemById(op.itemId()).ifPresent(item -> item.setQuantity(op.quantity()));
                }
            }
            case SET_PRICE -> {
                if (op.itemId() != null && op.price() != null) {
                    list.findItemById(op.itemId()).ifPresent(item -> item.setUnitPrice(op.price()));
                }
            }
            case TOGGLE_BOUGHT -> {
                if (op.itemId() != null) {
                    list.findItemById(op.itemId()).ifPresent(item -> item.toggleBought(op.userId()));
                }
            }
            case UPDATE_BUDGET -> {
                if (op.price() != null) {
                    list.setMaxBudget(op.price());
                }
            }
        }
    }
}
