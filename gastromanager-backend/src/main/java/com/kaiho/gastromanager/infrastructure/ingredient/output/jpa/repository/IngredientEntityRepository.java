package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IngredientEntityRepository extends JpaRepository<IngredientEntity, UUID>, JpaSpecificationExecutor<IngredientEntity> {

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM IngredientEntity i WHERE i.name = :name AND i.restaurant.uuid = :restaurantUuid")
    boolean existsByName(String name, UUID restaurantUuid);

    @Modifying
    @Query("UPDATE IngredientEntity i SET i.availableStock = :newStock WHERE i.uuid = :uuid")
    void updateStockByUuid(@Param("uuid") UUID uuid, @Param("newStock") double newStock);

    boolean existsByNameAndRestaurant(String name, RestaurantEntity restaurantEntity);

    @Query("SELECT i FROM IngredientEntity i WHERE i.restaurant.uuid = :restaurantUuid AND i.uuid = :uuid")
    Optional<IngredientEntity> findById(UUID uuid, UUID restaurantUuid);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM IngredientEntity i WHERE i.uuid = :uuid AND i.restaurant.uuid = :currentRestaurant")
    boolean existsByUuid(UUID uuid, UUID currentRestaurant);

}
