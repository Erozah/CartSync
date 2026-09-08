package com.erozah.kestachet.infrastructure.adapter.out.persistence;

import com.erozah.kestachet.domain.port.out.SyncJournalPort;
import com.erozah.kestachet.infrastructure.adapter.out.persistence.entity.SyncJournalEntity;
import com.erozah.kestachet.infrastructure.adapter.out.persistence.repository.SpringDataSyncJournalRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SyncJournalPersistenceAdapter implements SyncJournalPort {

    private final SpringDataSyncJournalRepository repository;

    public SyncJournalPersistenceAdapter(SpringDataSyncJournalRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isOperationAlreadyProcessed(String operationId) {
        return repository.existsById(operationId);
    }

    @Override
    public void recordProcessedOperation(String operationId, String listId, String userId) {
        repository.save(new SyncJournalEntity(operationId, listId, userId, Instant.now()));
    }
}
