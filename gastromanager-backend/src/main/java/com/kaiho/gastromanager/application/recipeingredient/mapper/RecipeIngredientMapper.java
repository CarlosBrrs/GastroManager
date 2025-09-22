package com.kaiho.gastromanager.application.recipeingredient.mapper;

import com.kaiho.gastromanager.application.baseproductingredient.dto.request.RecipeIngredientRequestDto;
import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientDetailResponseDto;
import com.kaiho.gastromanager.application.recipeingredient.dto.response.RecipeIngredientSummaryResponseDto;
import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeIngredientMapper {

    private final IngredientServicePort ingredientServicePort;

    public RecipeIngredient toRecipeIngredient(RecipeIngredientRequestDto recipeIngredientRequestDto, int yieldPortions) {
        if (recipeIngredientRequestDto == null) {
            return null;
        }
        Ingredient ingredient = ingredientServicePort.getIngredientById(recipeIngredientRequestDto.ingredientUuid());
        return RecipeIngredient.builder()
                               .ingredient(ingredient)
                               .quantity(recipeIngredientRequestDto.quantity() / yieldPortions).build();
    }

    public List<RecipeIngredientDetailResponseDto> toResponseList(List<RecipeIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }
        return ingredients.stream()
                          .map(this::toResponseDetail)
                          .toList();
    }

    private RecipeIngredientDetailResponseDto toResponseDetail(RecipeIngredient recipeIngredient) {
        if (recipeIngredient == null) {
            return null;
        }
        return RecipeIngredientDetailResponseDto.builder()
                                                .ingredientUuid(recipeIngredient.getIngredient().getUuid())
                                                .ingredientName(recipeIngredient.getIngredient().getName())
                                                .quantity(recipeIngredient.getQuantity())
                                                .build();
    }

    public List<RecipeIngredientSummaryResponseDto> toResponseSummaryList(List<RecipeIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }
        return ingredients.stream()
                          .map(this::toResponseSummary)
                          .toList();
    }

    private RecipeIngredientSummaryResponseDto toResponseSummary(RecipeIngredient recipeIngredient) {
        if (recipeIngredient == null) {
            return null;
        }
        return RecipeIngredientSummaryResponseDto.builder()
                                                 .ingredientUuid(recipeIngredient.getIngredient().getUuid())
                                                 .ingredientName(recipeIngredient.getIngredient().getName())
                                                 .quantity(recipeIngredient.getQuantity())
                                                 .build();
    }
}
