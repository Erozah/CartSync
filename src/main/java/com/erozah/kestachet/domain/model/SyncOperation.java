package com.erozah.kestachet.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record SyncOperation(
    String operationId,
    String listId,
    String userId,
    OperationType type,
    String itemId,
    String gtin,
    String itemName,
    Integer quantity,
    BigDecimal price,
    Instant timestamp
) {}
