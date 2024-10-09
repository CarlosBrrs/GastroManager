package com.kaiho.gastromanager.domain.ingredient.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExist;

import java.util.UUID;

public class IngredientDoesNotExistException extends EntityDoesNotExist {
    public IngredientDoesNotExistException(String uuid) {
        super("Ingredient with UUID: " + uuid + " does not exist");
    }
}
