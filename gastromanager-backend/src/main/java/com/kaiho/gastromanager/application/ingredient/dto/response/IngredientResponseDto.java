package com.kaiho.gastromanager.application.ingredient.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record IngredientResponseDto(
        UUID uuid,
        String name,
        int stockLevel,
        String unit,
        Double pricePerUnit,
        Instant lastUpdated) {
}
