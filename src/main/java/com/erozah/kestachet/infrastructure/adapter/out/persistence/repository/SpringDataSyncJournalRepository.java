package com.erozah.kestachet.infrastructure.adapter.out.persistence.repository;

import com.erozah.kestachet.infrastructure.adapter.out.persistence.entity.SyncJournalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSyncJournalRepository extends JpaRepository<SyncJournalEntity, String> {
}
