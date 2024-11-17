package com.kaiho.gastromanager.domain.inventorymovement.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryMovement(UUID ingredientUuid, int changeQuantity, String reason) {
}
