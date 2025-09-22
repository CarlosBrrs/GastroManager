package com.kaiho.gastromanager.domain.recipeingredient.model;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private Ingredient ingredient;
    private Recipe recipe;
    private Double quantity;
}
