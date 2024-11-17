package com.kaiho.gastromanager.domain.inventorymovement.spi;

import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;

import java.util.UUID;

public interface InventoryMovementPersistencePort {
    UUID createInventoryMovement(InventoryMovement inventoryMovement);
}
