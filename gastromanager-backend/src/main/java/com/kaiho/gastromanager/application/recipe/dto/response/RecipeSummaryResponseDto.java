package com.kaiho.gastromanager.application.recipe.dto.response;

import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientSummaryResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record RecipeSummaryResponseDto(
        UUID uuid,
        String name,
        String description,
        BigDecimal cost,
        List<RecipeIngredientSummaryResponseDto> ingredients,
        RecipeSummaryResponseDto baseRecipe,
        boolean isEnabled,
        String createdBy,
        String updatedBy,
        Instant createdDate,
        Instant updatedDate
) {
}
