package com.kaiho.gastromanager.domain.inventorymovement.api;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;

import java.util.UUID;

public interface InventoryMovementServicePort {

    void recordInventoryMovement(UUID ingredientUuid, int changeQuantity, String reason, Restaurant uuid);

}
