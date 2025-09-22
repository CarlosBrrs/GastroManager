package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderItemEntityRepository extends JpaRepository<OrderItemEntity, UUID> {

    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.uuid IN :orderItemUuids")
    List<OrderItemEntity> findByUuids(List<UUID> orderItemUuids);

    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.order.uuid = :orderUuid")
    List<OrderItemEntity> findByOrderUuid(UUID orderUuid);
}
