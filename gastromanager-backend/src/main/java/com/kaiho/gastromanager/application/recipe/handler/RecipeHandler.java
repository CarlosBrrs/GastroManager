package com.kaiho.gastromanager.application.recipe.handler;

import com.kaiho.gastromanager.application.recipe.dto.request.RecipeRequestDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeDetailResponseDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RecipeHandler {
    ApiGenericResponse<RecipeDetailResponseDto> createRecipe(RecipeRequestDto recipeRequestDto);

    ApiGenericResponse<Page<RecipeSummaryResponseDto>> getAllRecipes(RecipeSearchCriteria criteria);

    ApiGenericResponse<RecipeDetailResponseDto> getRecipeById(UUID recipeUuid);
}
