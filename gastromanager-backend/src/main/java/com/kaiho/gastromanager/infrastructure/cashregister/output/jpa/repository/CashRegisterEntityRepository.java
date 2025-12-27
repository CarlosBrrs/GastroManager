package com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashRegisterEntityRepository extends JpaRepository<CashRegisterEntity, UUID> {

    boolean existsByNameAndRestaurant(String name, RestaurantEntity restaurant);

    List<CashRegisterEntity> findByRestaurant(RestaurantEntity restaurant);

    boolean existsByUuidAndRestaurantUuid(UUID uuid, UUID restaurantUuid);

    @Query("SELECT c FROM CashRegisterEntity c LEFT JOIN FETCH c.sessions s " +
            "WHERE c.uuid = :cashRegisterUuid AND c.restaurant.uuid = :restaurantUuid " +
            "AND (s.status = 'OPEN' OR s IS NULL)")
    Optional<CashRegisterEntity> findByUuidAndRestaurantUuidWithSessions(
            @Param("cashRegisterUuid") UUID cashRegisterUuid,
            @Param("restaurantUuid") UUID restaurantUuid
    );

    @Query("SELECT DISTINCT c FROM CashRegisterEntity c " +
            "INNER JOIN FETCH c.sessions s " +
            "WHERE c.restaurant.uuid = :restaurantUuid " +
            "AND s.status = 'OPEN'")
    List<CashRegisterEntity> findAllByRestaurantUuidWithOpenSessions(
            @Param("restaurantUuid") UUID restaurantUuid
    );


    @Query("SELECT c FROM CashRegisterEntity c WHERE c.restaurant.uuid = :restaurantUuid")
    List<CashRegisterEntity> findAllByRestaurantUuid(@Param("restaurantUuid") UUID restaurantUuid);
}
