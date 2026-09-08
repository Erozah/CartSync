package com.erozah.kestachet.infrastructure.adapter.in.web;

import com.erozah.kestachet.domain.port.in.SyncBatchUseCase;
import com.erozah.kestachet.domain.port.in.SyncBatchUseCase.SyncBatchResult;
import com.erozah.kestachet.infrastructure.adapter.in.web.dto.SyncBatchRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncBatchUseCase syncBatchUseCase;

    public SyncController(SyncBatchUseCase syncBatchUseCase) {
        this.syncBatchUseCase = syncBatchUseCase;
    }

    @PostMapping
    public ResponseEntity<SyncBatchResult> syncBatch(@Valid @RequestBody SyncBatchRequest request) {
        SyncBatchResult result = syncBatchUseCase.processBatch(request.operations());
        return ResponseEntity.ok(result);
    }
}
