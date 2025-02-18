package com.kaiho.gastromanager.application.ingredient.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record IngredientResponseDto(
        UUID uuid,
        String name,
        double availableStock,
        String unit,
        double pricePerUnit,
        String supplier,
        int minimumStockQuantity,
        String createdBy,
        Instant createdDate,
        String updatedBy,
        Instant updatedDate) {
}
