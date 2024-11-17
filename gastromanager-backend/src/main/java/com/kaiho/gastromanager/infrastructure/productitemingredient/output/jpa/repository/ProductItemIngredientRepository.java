package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductItemIngredientRepository extends JpaRepository<ProductItemIngredientEntity, UUID> {

    List<ProductItemIngredientEntity> findByProductItemUuidIn(List<UUID> productItemUuids);
}
