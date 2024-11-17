package com.kaiho.gastromanager.domain.ingredient.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class IngredientAlreadyExistsException extends EntityAlreadyExistsException {
    public IngredientAlreadyExistsException(String name) {
        super("Ingredient with name " + name + " already exists in the system");
    }
}
