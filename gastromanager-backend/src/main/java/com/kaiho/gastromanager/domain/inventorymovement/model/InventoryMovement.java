package com.kaiho.gastromanager.domain.inventorymovement.model;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryMovement(UUID ingredientUuid, double changeQuantity, String reason, Restaurant restaurant) {
}
