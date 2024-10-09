package com.kaiho.gastromanager.domain.ingredient.api;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;

import java.util.List;
import java.util.UUID;

public interface IngredientServicePort {
    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(UUID uuid);

    UUID addIngredient(Ingredient ingredient);
}
