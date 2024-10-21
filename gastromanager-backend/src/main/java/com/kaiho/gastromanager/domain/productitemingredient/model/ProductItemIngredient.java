package com.kaiho.gastromanager.domain.productitemingredient.model;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductItemIngredient(UUID ingredientUuid, double quantity, Unit unit) {
}
