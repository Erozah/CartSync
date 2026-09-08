package com.erozah.kestachet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "list_items")
public class ItemEntity {

    @Id
    private String id;

    private String gtin;

    @Column(nullable = false)
    private String name;

    private int quantity;

    private String unit;

    private BigDecimal unitPrice;

    private boolean isBought;

    private String boughtByUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private ShoppingListEntity shoppingList;

    public ItemEntity() {}

    public ItemEntity(String id, String gtin, String name, int quantity, String unit, BigDecimal unitPrice, boolean isBought, String boughtByUserId) {
        this.id = id;
        this.gtin = gtin;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.isBought = isBought;
        this.boughtByUserId = boughtByUserId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getGtin() { return gtin; }
    public void setGtin(String gtin) { this.gtin = gtin; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public boolean isBought() { return isBought; }
    public void setBought(boolean bought) { isBought = bought; }
    public String getBoughtByUserId() { return boughtByUserId; }
    public void setBoughtByUserId(String boughtByUserId) { this.boughtByUserId = boughtByUserId; }
    public ShoppingListEntity getShoppingList() { return shoppingList; }
    public void setShoppingList(ShoppingListEntity shoppingList) { this.shoppingList = shoppingList; }
}
