package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import com.erozah.kestachet.domain.model.Item;
import java.math.BigDecimal;

public record ItemResponse(
    String id,
    String gtin,
    String name,
    int quantity,
    String unit,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    boolean isBought,
    String boughtByUserId
) {
    public static ItemResponse fromDomain(Item item) {
        return new ItemResponse(
            item.getId(),
            item.getGtin(),
            item.getName(),
            item.getQuantity(),
            item.getUnit(),
            item.getUnitPrice(),
            item.getTotalPrice(),
            item.isBought(),
            item.getBoughtByUserId()
        );
    }
}
