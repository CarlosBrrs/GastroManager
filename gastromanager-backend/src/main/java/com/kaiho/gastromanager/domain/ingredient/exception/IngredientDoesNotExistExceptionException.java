package com.kaiho.gastromanager.domain.ingredient.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class IngredientDoesNotExistExceptionException extends EntityDoesNotExistException {
    public IngredientDoesNotExistExceptionException(String uuid) {
        super("Ingredient with UUID: " + uuid + " does not exist");
    }
}
