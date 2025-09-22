package com.kaiho.gastromanager.infrastructure.product.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByNameAndRestaurant(String menuName, RestaurantEntity restaurantEntity);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM ProductEntity m WHERE m.name = :name AND m.restaurant.uuid = :restaurantUuid")
    boolean existsByName(String name, UUID restaurantUuid);

    @Query("SELECT m FROM ProductEntity m WHERE m.restaurant.uuid = :restaurantUuid AND m.uuid = :uuid")
    Optional<ProductEntity> findById(UUID uuid, UUID restaurantUuid);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM ProductEntity m WHERE m.uuid = :menuUuid AND m.restaurant.uuid = :restaurantUuid")
    boolean existsByUuidAndRestaurantUuid(UUID menuUuid, UUID restaurantUuid);

    @Query("SELECT p FROM ProductEntity p WHERE p.uuid IN :uuids AND p.restaurant.uuid = :restaurantUuid")
    List<ProductEntity> findAllByUuidInAndRestaurant(@Param("uuids") List<UUID> uuids, @Param("restaurantUuid") UUID restaurantUuid);

    @Query("SELECT p FROM ProductEntity p WHERE p.restaurant.uuid = :restaurantUuid ORDER BY p.category, p.name")
    List<ProductEntity> findAllByRestaurantUuid(@Param("restaurantUuid") UUID restaurantUuid);
}
