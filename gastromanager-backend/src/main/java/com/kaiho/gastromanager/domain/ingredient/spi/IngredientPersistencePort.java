package com.kaiho.gastromanager.domain.ingredient.spi;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IngredientPersistencePort {

    Page<Ingredient> getAllIngredients(IngredientSearchCriteria criteria, Pageable pageable);

    Optional<Ingredient> getIngredientByUuid(UUID uuid);

    boolean ingredientExistsByName(String name, UUID restaurantUuid);

    UUID addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(UUID uuid, Ingredient ingredient);

    UUID updateIngredientStock(UUID ingredientUuid, int newStock);

    List<Ingredient> findIngredientsByUuids(Set<UUID> uuids);

    void updateIngredientsStock(Map<UUID, Double> newAvailableStocks);

    Optional<Restaurant> getRestaurantByIngredientUuid(UUID ingredientUuid);
}
