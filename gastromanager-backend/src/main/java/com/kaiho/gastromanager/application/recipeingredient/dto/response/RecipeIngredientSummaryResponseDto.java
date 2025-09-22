package com.kaiho.gastromanager.application.recipeingredient.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RecipeIngredientSummaryResponseDto(
        UUID ingredientUuid,
        String ingredientName,
        double quantity
) {
}
