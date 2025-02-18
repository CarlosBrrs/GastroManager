package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantConfigEntityRepository extends JpaRepository<RestaurantConfigEntity, UUID> {

    @Query("SELECT rce FROM RestaurantConfigEntity rce WHERE rce.restaurant.uuid = :currentRestaurant")
    Optional<RestaurantConfigEntity> findByRestaurantUuid(UUID currentRestaurant);

}
