package com.kaiho.gastromanager.application.ingredient.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record IngredientSummaryResponseDto(
        UUID uuid,
        String name,
        double availableStock,
        String unit,
        BigDecimal pricePerUnit,
        String supplier,
        int minimumStockQuantity,
        String createdBy,
        Instant createdDate,
        String updatedBy,
        Instant updatedDate) {
}
