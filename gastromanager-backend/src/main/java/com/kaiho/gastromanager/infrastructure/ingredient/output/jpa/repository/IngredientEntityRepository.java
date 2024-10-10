package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientEntityRepository extends JpaRepository<IngredientEntity, UUID> {
}
