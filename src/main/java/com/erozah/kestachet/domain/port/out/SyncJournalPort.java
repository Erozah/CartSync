package com.erozah.kestachet.domain.port.out;

public interface SyncJournalPort {
    boolean isOperationAlreadyProcessed(String operationId);
    void recordProcessedOperation(String operationId, String listId, String userId);
}
