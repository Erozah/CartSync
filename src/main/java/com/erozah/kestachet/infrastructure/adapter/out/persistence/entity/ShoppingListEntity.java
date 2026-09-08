package com.erozah.kestachet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_lists")
public class ShoppingListEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String ownerId;

    private BigDecimal maxBudget;

    private boolean isArchived;

    private Instant createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "list_members", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "user_id")
    private Set<String> memberIds = new HashSet<>();

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemEntity> items = new ArrayList<>();

    public ShoppingListEntity() {}

    public ShoppingListEntity(String id, String title, String ownerId, BigDecimal maxBudget, boolean isArchived, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.maxBudget = maxBudget;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
    }

    public void addItem(ItemEntity item) {
        items.add(item);
        item.setShoppingList(this);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public BigDecimal getMaxBudget() { return maxBudget; }
    public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Set<String> getMemberIds() { return memberIds; }
    public void setMemberIds(Set<String> memberIds) { this.memberIds = memberIds; }
    public List<ItemEntity> getItems() { return items; }
    public void setItems(List<ItemEntity> items) { this.items = items; }
}
