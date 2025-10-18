package com.kaiho.gastromanager.domain.recipe.exception;

import java.util.UUID;

public class RecipeDoesNotExistException extends RuntimeException {
    public RecipeDoesNotExistException(UUID recipeUuid) {
        super("Recipe with UUID " + recipeUuid + " does not exist");
    }
}
