package com.kaiho.gastromanager.application.recipeingredient.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RecipeIngredientDetailResponseDto(
        UUID ingredientUuid,
        String ingredientName,
        double quantity
) {
}
