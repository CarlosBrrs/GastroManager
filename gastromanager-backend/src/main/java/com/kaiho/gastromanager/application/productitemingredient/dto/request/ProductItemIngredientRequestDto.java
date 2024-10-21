package com.kaiho.gastromanager.application.productitemingredient.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductItemIngredientRequestDto(UUID ingredientUuid, double quantity, String unit) {
}
