package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import com.erozah.kestachet.domain.model.SyncOperation;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SyncBatchRequest(
    @NotEmpty(message = "La liste des opérations ne peut pas être vide") List<SyncOperation> operations
) {}
