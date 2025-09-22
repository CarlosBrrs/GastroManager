package com.kaiho.gastromanager.application.recipe.dto.response;

import lombok.Builder;

@Builder
public record BaseRecipeDetailResponseDto(
        RecipeDetailResponseDto baseRecipe,
        Double portion
) {
}
