package com.kaiho.gastromanager.application.ingredient.dto.response;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record IngredientResponseDto(
        UUID uuid,
        String name,
        int availableStock,
        String unit,
        double pricePerUnit,
        String supplier,
        int minimumStockQuantity,
        String createdBy,
        Instant createdDate,
        String updatedBy,
        Instant updatedDate) {
}
