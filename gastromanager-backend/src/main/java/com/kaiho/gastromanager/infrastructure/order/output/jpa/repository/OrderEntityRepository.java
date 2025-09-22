package com.kaiho.gastromanager.infrastructure.order.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<OrderEntity> {
    boolean existsByCode(String base36);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.uuid = :restaurantUuid AND o.uuid = :uuid")
    Optional<OrderEntity> findById(UUID uuid, UUID restaurantUuid);

}
