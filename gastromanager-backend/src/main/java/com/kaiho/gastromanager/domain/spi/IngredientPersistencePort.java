package com.kaiho.gastromanager.domain.spi;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientPersistencePort {

    List<Ingredient> getAllIngredients();

    Optional<Ingredient> getIngredientById(UUID uuid);
}
