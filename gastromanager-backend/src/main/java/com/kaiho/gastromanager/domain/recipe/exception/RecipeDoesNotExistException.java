package com.kaiho.gastromanager.domain.recipe.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class RecipeDoesNotExistException extends EntityDoesNotExistException {
    public RecipeDoesNotExistException(UUID uuid) {

        super("Recipe with UUID: " + uuid + " does not exist");

    }
}
