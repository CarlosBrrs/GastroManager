package com.kaiho.gastromanager.domain.recipe.spi;

import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RecipePersistencePort {

    Recipe createRecipe(Recipe recipe);

    Page<Recipe> getAllRecipes(RecipeSearchCriteria criteria, Pageable pageable);

    Optional<Recipe> getRecipeByUuid(UUID recipeUuid);

    boolean recipeExistsByName(String name);

    boolean recipeExistsByUuid(UUID uuid, UUID currentRestaurant);
}
