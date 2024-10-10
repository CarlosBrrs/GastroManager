package com.kaiho.gastromanager.domain.ingredient.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExist;

public class IngredientDoesNotExistException extends EntityDoesNotExist {
    public IngredientDoesNotExistException(String uuid) {
        super("Ingredient with UUID: " + uuid + " does not exist");
    }
}
