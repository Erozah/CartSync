package com.erozah.kestachet.domain.port.in;

import com.erozah.kestachet.domain.model.SyncOperation;
import java.util.List;

public interface SyncBatchUseCase {
    record SyncBatchResult(int appliedCount, int duplicateCount, List<String> errors) {}

    SyncBatchResult processBatch(List<SyncOperation> operations);
}
