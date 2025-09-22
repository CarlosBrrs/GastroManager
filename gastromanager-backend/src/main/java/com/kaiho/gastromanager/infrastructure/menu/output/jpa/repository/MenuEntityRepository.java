package com.kaiho.gastromanager.infrastructure.menu.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MenuEntityRepository extends JpaRepository<MenuEntity, UUID>, JpaSpecificationExecutor<MenuEntity> {

    boolean existsByNameAndRestaurant(String menuName, RestaurantEntity restaurantEntity);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM ProductEntity m WHERE m.name = :name AND m.restaurant.uuid = :restaurantUuid")
    boolean existsByName(String name, UUID restaurantUuid);

    @Query("SELECT m FROM ProductEntity m WHERE m.restaurant.uuid = :restaurantUuid AND m.uuid = :uuid")
    Optional<MenuEntity> findById(UUID uuid, UUID restaurantUuid);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM ProductEntity m WHERE m.uuid = :menuUuid AND m.restaurant.uuid = :restaurantUuid")
    boolean existsByUuidAndRestaurantUuid(UUID menuUuid, UUID restaurantUuid);
}
