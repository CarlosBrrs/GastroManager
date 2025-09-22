package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RecipeEntityRepository extends JpaRepository<RecipeEntity, UUID>, JpaSpecificationExecutor<RecipeEntity> {

    @Query("SELECT r FROM RecipeEntity r WHERE r.uuid = :recipeUuid AND r.restaurant.uuid = :currentRestaurant")
    Optional<RecipeEntity> findById(UUID recipeUuid, UUID currentRestaurant);
}
