package com.kaiho.gastromanager.application.recipe.mapper;

import com.kaiho.gastromanager.application.recipe.dto.request.RecipeRequestDto;
import com.kaiho.gastromanager.application.recipe.dto.response.BaseRecipeDetailResponseDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeDetailResponseDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeSummaryResponseDto;
import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientDetailResponseDto;
import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientSummaryResponseDto;
import com.kaiho.gastromanager.application.recipeingredient.mapper.RecipeIngredientMapper;
import com.kaiho.gastromanager.domain.recipe.api.RecipeServicePort;
import com.kaiho.gastromanager.domain.recipe.model.BaseRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class RecipeMapper {


    private final RecipeServicePort recipeServicePort;
    private final RecipeIngredientMapper recipeIngredient;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public Recipe toDomain(RecipeRequestDto recipeRequestDto) {
        if (recipeRequestDto == null) {
            return null;
        }
        List<RecipeIngredient> ingredients = recipeRequestDto.ingredients().stream()
                                                             .map(recipeIngredientRequestDto -> recipeIngredient.toRecipeIngredient(recipeIngredientRequestDto, recipeRequestDto.yieldPortions()))
                                                             .collect(Collectors.toList());
        Recipe recipe = Recipe.builder()
                              .name(recipeRequestDto.name())
                              .description(recipeRequestDto.description())
                              .restaurant(Restaurant.builder().uuid(getCurrentRestaurant()).build())
                              .ingredients(ingredients)
                              .isEnabled(true)
                              .build();

        if (recipeRequestDto.baseRecipe() != null) {
            Recipe base = recipeServicePort.getRecipeById(recipeRequestDto.baseRecipe().uuid());
            BaseRecipe baseRecipe = BaseRecipe.builder()
                                              .recipe(base)
                                              .portion(recipeRequestDto.baseRecipe().portion())
                                              .build();
            recipe.setBaseRecipe(baseRecipe);
        }

        return recipe;
    }

    public RecipeDetailResponseDto toResponseDetail(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        BaseRecipeDetailResponseDto baseRecipeDetailResponseDto = null;
        if (recipe.getBaseRecipe() != null) {
            baseRecipeDetailResponseDto = BaseRecipeDetailResponseDto.builder()
                                                                     .baseRecipe(toResponseDetail(recipe.getBaseRecipe().getRecipe()))
                                                                     .portion(recipe.getBaseRecipe().getPortion())
                                                                     .build();
        }
        List<RecipeIngredientDetailResponseDto> ingredients = recipeIngredientMapper.toResponseList(recipe.getIngredients());
        return RecipeDetailResponseDto.builder()
                                      .uuid(recipe.getUuid())
                                      .name(recipe.getName())
                                      .description(recipe.getDescription())
                                      .cost(recipe.getCost())
                                      .isEnabled(recipe.isEnabled())
                                      .baseRecipe(baseRecipeDetailResponseDto)
                                      .ingredients(ingredients)
                                      .createdBy(recipe.getCreatedBy())
                                      .createdDate(recipe.getCreatedDate())
                                      .updatedBy(recipe.getUpdatedBy())
                                      .updatedDate(recipe.getUpdatedDate())
                                      .build();
    }

    public RecipeSummaryResponseDto toResponseSummary(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        List<RecipeIngredientSummaryResponseDto> ingredients = recipeIngredientMapper.toResponseSummaryList(recipe.getIngredients());
        return RecipeSummaryResponseDto.builder()
                                       .uuid(recipe.getUuid())
                                       .name(recipe.getName())
                                       .description(recipe.getDescription())
                                       .cost(recipe.getCost())
                                       .isEnabled(recipe.isEnabled())
                                       .baseRecipe(recipe.getBaseRecipe() != null ? toResponseSummary(recipe.getBaseRecipe().getRecipe()) : null)
                                       .ingredients(ingredients)
                                       .createdBy(recipe.getCreatedBy())
                                       .createdDate(recipe.getCreatedDate())
                                       .updatedBy(recipe.getUpdatedBy())
                                       .updatedDate(recipe.getUpdatedDate())
                                       .build();
    }
}
