package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantEntityRepository extends JpaRepository<RestaurantEntity, UUID> {
    @Query("SELECT COUNT(r) > 0 FROM RestaurantEntity r WHERE r.name = :name AND r.address = :address AND r.owner.uuid = :ownerUuid")
    boolean existsByNameAndAddressAndOwnerUuid(@Param("name") String name, @Param("address") String address, @Param("ownerUuid") UUID ownerUuid);

    Optional<RestaurantEntity> findByName(String restName);
}
