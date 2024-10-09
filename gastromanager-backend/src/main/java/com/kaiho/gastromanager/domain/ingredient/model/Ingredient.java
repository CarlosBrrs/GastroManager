package com.kaiho.gastromanager.domain.ingredient.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Ingredient(
        UUID uuid,
        String name,
        int stockLevel,
        Unit unit,
        Double pricePerUnit,
        String createdBy,
        Instant createdDateTime,
        String updatedBy, Instant lastUpdated) {
}
