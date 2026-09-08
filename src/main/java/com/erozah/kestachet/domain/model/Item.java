package com.erozah.kestachet.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private final String id;
    private final String gtin;
    private String name;
    private int quantity;
    private String unit;
    private BigDecimal unitPrice;
    private boolean isBought;
    private String boughtByUserId;

    public Item(String id, String gtin, String name, int quantity, String unit, BigDecimal unitPrice, boolean isBought, String boughtByUserId) {
        this.id = Objects.requireNonNull(id, "L'id de l'article est requis");
        this.gtin = gtin;
        this.name = Objects.requireNonNull(name, "Le nom de l'article est requis");
        this.quantity = Math.max(1, quantity);
        this.unit = unit != null ? unit : "unité";
        this.unitPrice = unitPrice != null ? unitPrice : BigDecimal.ZERO;
        this.isBought = isBought;
        this.boughtByUserId = boughtByUserId;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice : BigDecimal.ZERO;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public void toggleBought(String userId) {
        this.isBought = !this.isBought;
        this.boughtByUserId = this.isBought ? userId : null;
    }

    public String getId() { return id; }
    public String getGtin() { return gtin; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public boolean isBought() { return isBought; }
    public String getBoughtByUserId() { return boughtByUserId; }
}
