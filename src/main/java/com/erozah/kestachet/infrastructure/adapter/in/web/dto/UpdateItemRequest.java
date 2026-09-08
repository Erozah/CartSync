package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record UpdateItemRequest(
    int quantity,
    BigDecimal price
) {}
