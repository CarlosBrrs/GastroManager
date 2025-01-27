package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductItemIngredientRepository extends JpaRepository<ProductItemIngredientEntity, UUID> {

    @Query("SELECT p FROM ProductItemIngredientEntity p " +
            "WHERE p.productItem.uuid IN :productItemUuids " +
            "AND p.productItem.restaurant.uuid = :restaurantUuid")
    List<ProductItemIngredientEntity> findByProductItemUuidIn(List<UUID> productItemUuids, UUID restaurantUuid);
}
