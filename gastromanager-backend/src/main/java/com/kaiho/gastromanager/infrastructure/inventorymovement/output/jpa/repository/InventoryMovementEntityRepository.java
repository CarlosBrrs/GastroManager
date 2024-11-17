package com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.entity.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryMovementEntityRepository extends JpaRepository<InventoryMovementEntity, UUID> {
}
