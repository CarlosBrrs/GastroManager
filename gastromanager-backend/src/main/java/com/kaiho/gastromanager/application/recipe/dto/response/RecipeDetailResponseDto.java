package com.kaiho.gastromanager.application.recipe.dto.response;

import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientDetailResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record RecipeDetailResponseDto(UUID uuid,
                                      String name,
                                      String description,
                                      BigDecimal cost,
                                      boolean isEnabled,
                                      BaseRecipeDetailResponseDto baseRecipe,
                                      List<RecipeIngredientDetailResponseDto> ingredients,
                                      String createdBy,
                                      Instant createdDate,
                                      String updatedBy,
                                      Instant updatedDate) {
}
