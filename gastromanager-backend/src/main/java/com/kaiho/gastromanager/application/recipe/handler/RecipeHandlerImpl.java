package com.kaiho.gastromanager.application.recipe.handler;

import com.kaiho.gastromanager.application.recipe.dto.request.RecipeRequestDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeDetailResponseDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeSummaryResponseDto;
import com.kaiho.gastromanager.application.recipe.mapper.RecipeMapper;
import com.kaiho.gastromanager.domain.recipe.api.RecipeServicePort;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class RecipeHandlerImpl implements RecipeHandler {

    private final RecipeMapper recipeMapper;
    private final RecipeServicePort recipeServicePort;

    @Override
    public ApiGenericResponse<RecipeDetailResponseDto> createRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe = recipeMapper.toDomain(recipeRequestDto);
        Recipe createdRecipe = recipeServicePort.createRecipe(recipe);
        RecipeDetailResponseDto response = recipeMapper.toResponseDetail(createdRecipe);
        return buildSuccessResponse("Recipe created successfully", response);
    }

    @Override
    public ApiGenericResponse<Page<RecipeSummaryResponseDto>> getAllRecipes(RecipeSearchCriteria criteria) {
        Page<Recipe> recipePage = recipeServicePort.getAllRecipes(criteria);
        Page<RecipeSummaryResponseDto> recipeSummaryResponseDtoList = recipePage.map(recipeMapper::toResponseSummary);
        return buildSuccessResponse("List of recipes retrieved successfully", recipeSummaryResponseDtoList);
    }

    @Override
    public ApiGenericResponse<RecipeDetailResponseDto> getRecipeById(UUID recipeUuid) {
        Recipe recipe = recipeServicePort.getRecipeById(recipeUuid);
        RecipeDetailResponseDto response = recipeMapper.toResponseDetail(recipe);
        return buildSuccessResponse("Recipe retrieved successfully", response);
    }
}
