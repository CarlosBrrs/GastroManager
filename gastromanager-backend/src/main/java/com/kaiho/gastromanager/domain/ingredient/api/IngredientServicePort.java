package com.kaiho.gastromanager.domain.ingredient.api;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IngredientServicePort {
    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(UUID uuid);

    UUID addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(UUID uuid, Ingredient ingredient);

    UUID adjustIngredientStock(UUID ingredientUuid, int newStock, String reason);

    Map<UUID, Ingredient> getIngredientsByUuids(Set<UUID> uuids);

    void batchAdjustStock(Map<UUID, Integer> stockAdjustments, String orderPlacement);
}
