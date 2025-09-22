package com.kaiho.gastromanager.domain.ingredient.api;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IngredientServicePort {
    Page<Ingredient> getAllIngredients(IngredientSearchCriteria criteria);

    Ingredient getIngredientById(UUID uuid);

//    Ingredient getIngredientById(UUID uuid, UUID currentRestaurant);

    UUID addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(UUID uuid, Ingredient ingredient);

    UUID adjustIngredientStock(UUID ingredientUuid, int newStock, String reason);

    List<Ingredient> getIngredientsByUuid(Set<UUID> uuids);

    void batchAdjustStock(Map<UUID, Double> stockAdjustments, String orderPlacement);

    Restaurant getRestaurantByIngredientUuid(UUID ingredientUuid);
}
