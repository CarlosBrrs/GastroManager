package com.kaiho.gastromanager.domain.recipe.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class RecipeAlreadyExistsException extends EntityAlreadyExistsException {
    public RecipeAlreadyExistsException(String recipeName) {
        super("Recipe with name '" + recipeName + "' already exists in the system.");
    }
}

