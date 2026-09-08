package com.erozah.kestachet.infrastructure.adapter.out.persistence.repository;

import com.erozah.kestachet.infrastructure.adapter.out.persistence.entity.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataShoppingListRepository extends JpaRepository<ShoppingListEntity, String> {

    @Query("SELECT l FROM ShoppingListEntity l WHERE l.ownerId = :userId OR :userId MEMBER OF l.memberIds")
    List<ShoppingListEntity> findAllAccessibleByUserId(@Param("userId") String userId);
}
