package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductItemRepository extends JpaRepository<ProductItemEntity, UUID> {
    boolean existsByName(String name);

    List<ProductItemEntity> findByUuidIn(List<UUID> uuids);

}
