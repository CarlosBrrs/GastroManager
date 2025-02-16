package com.kaiho.gastromanager.application.productitemingredient.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductItemIngredientResponseDto(UUID ingredientUuid, String name, String unit, double quantity) {
}
