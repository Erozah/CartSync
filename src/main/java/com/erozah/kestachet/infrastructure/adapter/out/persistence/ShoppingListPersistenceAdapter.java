package com.erozah.kestachet.infrastructure.adapter.out.persistence;

import com.erozah.kestachet.domain.model.Item;
import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;
import com.erozah.kestachet.infrastructure.adapter.out.persistence.entity.ItemEntity;
import com.erozah.kestachet.infrastructure.adapter.out.persistence.entity.ShoppingListEntity;
import com.erozah.kestachet.infrastructure.adapter.out.persistence.repository.SpringDataShoppingListRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ShoppingListPersistenceAdapter implements ShoppingListRepositoryPort {

    private final SpringDataShoppingListRepository repository;

    public ShoppingListPersistenceAdapter(SpringDataShoppingListRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShoppingList save(ShoppingList domain) {
        ShoppingListEntity entity = toEntity(domain);
        ShoppingListEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ShoppingList> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ShoppingList> findAllByUserId(String userId) {
        return repository.findAllAccessibleByUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private ShoppingListEntity toEntity(ShoppingList domain) {
        ShoppingListEntity entity = new ShoppingListEntity(
            domain.getId(),
            domain.getTitle(),
            domain.getOwnerId(),
            domain.getMaxBudget(),
            domain.isArchived(),
            domain.getCreatedAt()
        );
        entity.setMemberIds(new java.util.HashSet<>(domain.getMemberIds()));

        for (Item item : domain.getItems()) {
            ItemEntity itemEntity = new ItemEntity(
                item.getId(),
                item.getGtin(),
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getUnitPrice(),
                item.isBought(),
                item.getBoughtByUserId()
            );
            entity.addItem(itemEntity);
        }
        return entity;
    }

    private ShoppingList toDomain(ShoppingListEntity entity) {
        ShoppingList domain = new ShoppingList(
            entity.getId(),
            entity.getTitle(),
            entity.getOwnerId(),
            entity.getMaxBudget()
        );
        domain.setArchived(entity.isArchived());

        for (String memberId : entity.getMemberIds()) {
            domain.addMember(memberId);
        }

        for (ItemEntity itemEntity : entity.getItems()) {
            Item item = new Item(
                itemEntity.getId(),
                itemEntity.getGtin(),
                itemEntity.getName(),
                itemEntity.getQuantity(),
                itemEntity.getUnit(),
                itemEntity.getUnitPrice(),
                itemEntity.isBought(),
                itemEntity.getBoughtByUserId()
            );
            domain.addItem(item);
        }

        return domain;
    }
}
