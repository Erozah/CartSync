package com.erozah.kestachet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "sync_journal")
public class SyncJournalEntity {

    @Id
    private String operationId;

    @Column(nullable = false)
    private String listId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Instant processedAt;

    public SyncJournalEntity() {}

    public SyncJournalEntity(String operationId, String listId, String userId, Instant processedAt) {
        this.operationId = operationId;
        this.listId = listId;
        this.userId = userId;
        this.processedAt = processedAt;
    }

    public String getOperationId() { return operationId; }
    public String getListId() { return listId; }
    public String getUserId() { return userId; }
    public Instant getProcessedAt() { return processedAt; }
}
