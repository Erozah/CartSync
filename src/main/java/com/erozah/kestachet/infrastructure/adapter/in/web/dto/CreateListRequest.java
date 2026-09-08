package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateListRequest(
    @NotBlank(message = "Le titre est obligatoire") String title,
    BigDecimal maxBudget
) {}
