package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SubmenuEntityRepository extends JpaRepository<SubmenuEntity, UUID>, JpaSpecificationExecutor<SubmenuEntity> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM SubmenuEntity s WHERE s.name = :name AND s.restaurant.uuid = :restaurantUuid")
    boolean existsByName(String name, UUID restaurantUuid);

    @Query("SELECT s FROM SubmenuEntity s WHERE s.uuid = :uuid AND s.restaurant.uuid = :restaurantUuid")
    Optional<SubmenuEntity> findByUuid(UUID uuid, UUID restaurantUuid);
}

