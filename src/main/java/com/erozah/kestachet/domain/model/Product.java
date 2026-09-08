package com.erozah.kestachet.domain.model;

import java.math.BigDecimal;

public record Product(
    String gtin,
    String name,
    String brand,
    String category,
    BigDecimal estimatedPrice,
    String imageUrl
) {}
