package com.kaiho.gastromanager.domain.productitemingredient.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductItemIngredient(UUID uuid, UUID productItemUuid, UUID ingredientUuid, double quantity) {
}
