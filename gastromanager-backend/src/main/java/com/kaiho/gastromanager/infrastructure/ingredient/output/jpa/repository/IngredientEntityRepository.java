package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IngredientEntityRepository extends JpaRepository<IngredientEntity, UUID> {

    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE IngredientEntity i SET i.availableStock = :newStock WHERE i.uuid = :uuid")
    void updateStockByUuid(@Param("uuid") UUID uuid, @Param("newStock") double newStock);
}
