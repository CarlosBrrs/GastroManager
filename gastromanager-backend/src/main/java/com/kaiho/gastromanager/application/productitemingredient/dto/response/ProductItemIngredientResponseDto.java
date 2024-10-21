package com.kaiho.gastromanager.application.productitemingredient.dto.response;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductItemIngredientResponseDto(UUID ingredientUuid, double quantity, Unit unit) {
}
