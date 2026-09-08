package com.erozah.kestachet.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class ShoppingList {
    private final String id;
    private String title;
    private final String ownerId;
    private final Set<String> memberIds;
    private final List<Item> items;
    private BigDecimal maxBudget;
    private boolean isArchived;
    private final Instant createdAt;

    public ShoppingList(String id, String title, String ownerId, BigDecimal maxBudget) {
        this.id = Objects.requireNonNull(id, "L'id de la liste est requis");
        this.title = Objects.requireNonNull(title, "Le titre est requis");
        this.ownerId = Objects.requireNonNull(ownerId, "Le propriétaire est requis");
        this.memberIds = new HashSet<>();
        this.items = new ArrayList<>();
        this.maxBudget = maxBudget != null ? maxBudget : BigDecimal.ZERO;
        this.isArchived = false;
        this.createdAt = Instant.now();
    }

    public boolean isAccessibleBy(String userId) {
        return ownerId.equals(userId) || memberIds.contains(userId);
    }

    public void addMember(String userId) {
        if (userId != null && !userId.equals(ownerId)) {
            memberIds.add(userId);
        }
    }

    public void removeMember(String userId) {
        memberIds.remove(userId);
    }

    public void addItem(Item item) {
        items.add(Objects.requireNonNull(item));
    }

    public Optional<Item> findItemById(String itemId) {
        return items.stream().filter(i -> i.getId().equals(itemId)).findFirst();
    }

    public void removeItem(String itemId) {
        items.removeIf(i -> i.getId().equals(itemId));
    }

    public BigDecimal calculateTotalCart() {
        return items.stream()
                    .map(Item::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Budget computeBudget() {
        return Budget.of(maxBudget, calculateTotalCart());
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOwnerId() { return ownerId; }
    public Set<String> getMemberIds() { return Collections.unmodifiableSet(memberIds); }
    public List<Item> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getMaxBudget() { return maxBudget; }
    public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }
    public Instant getCreatedAt() { return createdAt; }
}
