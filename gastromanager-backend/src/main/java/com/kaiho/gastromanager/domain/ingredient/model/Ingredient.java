package com.kaiho.gastromanager.domain.ingredient.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Ingredient(
        UUID uuid,
        String name,
        int availableStock,
        Unit unit,
        double pricePerUnit,
        String supplier,
        int minimumStockQuantity,
        String createdBy,
        Instant createdDate,
        String updatedBy,
        Instant updatedDate) {
}
