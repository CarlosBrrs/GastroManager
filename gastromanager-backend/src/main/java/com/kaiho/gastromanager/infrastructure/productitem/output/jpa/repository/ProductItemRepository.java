package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductItemRepository extends JpaRepository<ProductItemEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(pi) > 0 THEN TRUE ELSE FALSE END FROM ProductItemEntity pi WHERE pi.name = :name AND pi.restaurant.uuid = :restaurantUuid")
    boolean existsByName(String name, UUID restaurantUuid);

    List<ProductItemEntity> findByUuidIn(List<UUID> uuids);

    @Query("SELECT pi FROM ProductItemEntity pi WHERE pi.restaurant.uuid = :restaurantUuid AND pi.uuid = :uuid")
    Optional<ProductItemEntity> findById(UUID uuid, UUID restaurantUuid);
}
