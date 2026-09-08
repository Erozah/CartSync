package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record AddItemRequest(
    String gtin,
    String name,
    Integer quantity,
    String unit,
    BigDecimal price
) {}
