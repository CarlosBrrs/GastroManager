package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.AvailableStockNotUpdatedException;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientAlreadyExistsException;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.exception.UnacceptableStockQuantitiesException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.domain.inventorymovement.api.InventoryMovementServicePort;
import com.kaiho.gastromanager.domain.order.exception.InsufficientStockException;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@RequiredArgsConstructor
@Service
public class IngredientUseCase implements IngredientServicePort {

    private final IngredientPersistencePort ingredientPersistencePort;
    private final InventoryMovementServicePort inventoryMovementServicePort;
    private final RestaurantServicePort restaurantServicePort;

    private static void validateStockQuantities(int minimumStockQuantity, double availableStock) {
        if (minimumStockQuantity >= availableStock) {
            throw new UnacceptableStockQuantitiesException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> getAllIngredients() {
        return ingredientPersistencePort.getAllIngredients();
    }

    @Override
    @Transactional(readOnly = true)
    public Ingredient getIngredientById(UUID uuid, UUID restaurantUuid) {
        return ingredientPersistencePort.getIngredientByUuid(uuid, restaurantUuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
    }

    @Override
    @Transactional
    public UUID addIngredient(Ingredient ingredient) {
        if (ingredientPersistencePort.ingredientExistsByName(ingredient.getName(), ingredient.getRestaurant().getUuid())) {
            throw new IngredientAlreadyExistsException(ingredient.getName());
        }
        validateStockQuantities(ingredient.getMinimumStockQuantity(), ingredient.getAvailableStock());
        UUID ingredientUuid = ingredientPersistencePort.addIngredient(ingredient);
        inventoryMovementServicePort.recordInventoryMovement(ingredientUuid, ingredient.getAvailableStock(), "Initial stock", ingredient.getRestaurant());
        return ingredientUuid;
    }

    @Override
    @Transactional
    public Ingredient updateIngredient(UUID uuid, Ingredient ingredient) {

        Ingredient ingredientById = ingredientPersistencePort.getIngredientByUuid(uuid, ingredient.getRestaurant().getUuid())
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
        if (!ingredientById.getName().equals(ingredient.getName()) &&
                ingredientPersistencePort.ingredientExistsByName(ingredient.getName(), ingredient.getRestaurant().getUuid())) {
            throw new IngredientAlreadyExistsException(ingredient.getName());
        }
        validateStockQuantities(ingredient.getMinimumStockQuantity(), ingredientById.getAvailableStock());
        return ingredientPersistencePort.updateIngredient(uuid, ingredient);
    }

    @Override
    @Transactional
    public UUID adjustIngredientStock(UUID ingredientUuid, int newStock, String reason, UUID restaurantUuid) {
        Ingredient ingredientToUpdate = ingredientPersistencePort.getIngredientByUuid(ingredientUuid, restaurantUuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));

        if (newStock == ingredientToUpdate.getAvailableStock()) {
            throw new AvailableStockNotUpdatedException();
        }
//        validateStockQuantities(ingredientToUpdate.minimumStockQuantity(), newStock);
        UUID updatedIngredientUuid = ingredientPersistencePort.updateIngredientStock(ingredientUuid, newStock);

        // Registrar la diferencia de stock en caso de que haya un cambio en el inventario
        double stockDifference = newStock - ingredientToUpdate.getAvailableStock();
//        String reason = stockDifference > 0 ? "Adjust of stock for increment" : "Adjust of stock for reduction";
        inventoryMovementServicePort.recordInventoryMovement(updatedIngredientUuid, stockDifference, reason, ingredientToUpdate.getRestaurant());
        return updatedIngredientUuid;
    }

    @Override
    public List<Ingredient> getIngredientsByUuid(Set<UUID> uuids) {
        // Consulta los ingredientes desde el puerto de persistencia
        return ingredientPersistencePort.findIngredientsByUuids(uuids);
    }

    @Override
    public void batchAdjustStock(Map<UUID, Double> stockAdjustments, String reason) {

        Set<UUID> ingredientUuids = stockAdjustments.keySet();
        List<Ingredient> ingredients = ingredientPersistencePort.findIngredientsByUuids(ingredientUuids);

        Map<UUID, Double> newAvailableStocks = new HashMap<>();

        for (Ingredient ingredient : ingredients) {
            Double adjustment = stockAdjustments.get(ingredient.getUuid());
            double newStock = (ingredient.getAvailableStock() - adjustment);

            if (newStock < 0) {
                throw new InsufficientStockException(ingredient.getName(), adjustment, ingredient.getAvailableStock());
            }
            newAvailableStocks.put(ingredient.getUuid(), newStock);
        }

        ingredientPersistencePort.updateIngredientsStock(newAvailableStocks);

        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());
        for (Map.Entry<UUID, Double> newAvailableStock : newAvailableStocks.entrySet()) {
            inventoryMovementServicePort.recordInventoryMovement(newAvailableStock.getKey(), -stockAdjustments.get(newAvailableStock.getKey()), reason, restaurant);
        }

    }

    @Override
    public Restaurant getRestaurantByIngredientUuid(UUID ingredientUuid) {
        return ingredientPersistencePort.getRestaurantByIngredientUuid(ingredientUuid).orElseThrow(() -> new RestaurantDoesNotExistException("..."));
    }
}
