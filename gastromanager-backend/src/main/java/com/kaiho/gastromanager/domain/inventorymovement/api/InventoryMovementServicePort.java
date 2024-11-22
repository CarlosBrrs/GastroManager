package com.kaiho.gastromanager.domain.inventorymovement.api;

import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;

import java.util.Map;
import java.util.UUID;

public interface InventoryMovementServicePort {
//    UUID createInventoryMovement(InventoryMovement movement);

    void recordInventoryMovement(UUID ingredientUuid, int changeQuantity, String reason);

    void recordInventoryMovements(Map<UUID, Integer> changeQuantities, String reason );
}
