package com.kaiho.gastromanager.domain.ingredient.spi;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IngredientPersistencePort {

    List<Ingredient> getAllIngredients();

    Optional<Ingredient> getIngredientByUuid(UUID uuid, UUID restaurantUuid);

    boolean ingredientExistsByName(String name, UUID restaurantUuid);

    UUID addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(UUID uuid, Ingredient ingredient);

    UUID updateIngredientStock(UUID ingredientUuid, int newStock);

    List<Ingredient> findIngredientsByUuids(Set<UUID> uuids);

    void updateIngredientsStock(Map<UUID, Integer> newAvailableStocks);

    Optional<Restaurant> getRestaurantByIngredientUuid(UUID ingredientUuid);
}
