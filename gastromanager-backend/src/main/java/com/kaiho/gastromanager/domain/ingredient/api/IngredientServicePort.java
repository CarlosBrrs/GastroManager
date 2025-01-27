package com.kaiho.gastromanager.domain.ingredient.api;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IngredientServicePort {
    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(UUID uuid, UUID currentRestaurant);

    UUID addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(UUID uuid, Ingredient ingredient);

    UUID adjustIngredientStock(UUID ingredientUuid, int newStock, String reason, UUID currentRestaurant);

    List<Ingredient> getIngredientsByUuid(Set<UUID> uuids);

    void batchAdjustStock(Map<UUID, Integer> stockAdjustments, String orderPlacement);

    Restaurant getRestaurantByIngredientUuid(UUID ingredientUuid);
}
