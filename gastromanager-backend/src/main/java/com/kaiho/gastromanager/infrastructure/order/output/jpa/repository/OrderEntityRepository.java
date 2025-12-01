package com.kaiho.gastromanager.infrastructure.order.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<OrderEntity> {
    boolean existsByCode(String base36);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.uuid = :restaurantUuid AND o.uuid = :uuid")
    Optional<OrderEntity> findById(UUID uuid, UUID restaurantUuid);

    @Query("SELECT DISTINCT o FROM OrderEntity o " +
            "LEFT JOIN FETCH o.orderItems " +
            "WHERE o.restaurant.uuid = :restaurantUuid")
    List<OrderEntity> findAllWithOrderItemsByRestaurantUuid(@Param("restaurantUuid") UUID restaurantUuid);

    @Query("SELECT DISTINCT o FROM OrderEntity o " +
            "LEFT JOIN FETCH o.orderItems " +
            "WHERE o IN :orders")
    List<OrderEntity> findAllWithOrderItems(@Param("orders") List<OrderEntity> orders);

    @Query("SELECT DISTINCT o FROM OrderEntity o " +
            "LEFT JOIN FETCH o.payments " +
            "WHERE o.uuid IN :orderUuids")
    List<OrderEntity> findAllWithPayments(@Param("orderUuids") List<UUID> orderUuids);
}
