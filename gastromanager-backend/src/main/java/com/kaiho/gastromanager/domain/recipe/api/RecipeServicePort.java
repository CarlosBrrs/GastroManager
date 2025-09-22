package com.kaiho.gastromanager.domain.recipe.api;

import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RecipeServicePort {
    Recipe createRecipe(Recipe recipe);

    Page<Recipe> getAllRecipes(RecipeSearchCriteria criteria);

    Recipe getRecipeById(UUID recipeUuid);
}
