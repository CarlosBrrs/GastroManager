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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IngredientUseCase implements IngredientServicePort {

    private final IngredientPersistencePort ingredientPersistencePort;
    private final InventoryMovementServicePort inventoryMovementServicePort;

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

    private static void validateStockQuantities(int minimumStockQuantity, int availableStock) {
        if (minimumStockQuantity >= availableStock) {
            throw new UnacceptableStockQuantitiesException();
        }
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
        int stockDifference = newStock - ingredientToUpdate.getAvailableStock();
//        String reason = stockDifference > 0 ? "Adjust of stock for increment" : "Adjust of stock for reduction";
        inventoryMovementServicePort.recordInventoryMovement(updatedIngredientUuid, stockDifference, reason, ingredientToUpdate.getRestaurant());
        return updatedIngredientUuid;
    }

    @Override
    public Map<UUID, Ingredient> getIngredientsByUuids(Set<UUID> uuids) {
        // Consulta los ingredientes desde el puerto de persistencia
        List<Ingredient> ingredients = ingredientPersistencePort.findIngredientsByUuids(uuids);

        // Convertir la lista de ingredientes en un mapa con el UUID como clave
        return ingredients.stream()
                .collect(Collectors.toMap(Ingredient::getUuid, ingredient -> ingredient));
    }

    @Override
    public void batchAdjustStock(Map<UUID, Integer> stockAdjustments, String reason) {
        // Obtener los ingredientes afectados
        Set<UUID> ingredientUuids = stockAdjustments.keySet();
        List<Ingredient> ingredients = ingredientPersistencePort.findIngredientsByUuids(ingredientUuids);

        Map<UUID, Integer> newAvailableStocks = new HashMap<>();

        // Actualizar el stock de cada ingrediente
        for (Ingredient ingredient : ingredients) {
            Integer adjustment = stockAdjustments.get(ingredient.getUuid());
            int newStock = (ingredient.getAvailableStock() - adjustment);

            if (newStock < 0) {
                throw new InsufficientStockException(ingredient.getName(), adjustment, ingredient.getAvailableStock());
            }
            newAvailableStocks.put(ingredient.getUuid(), newStock);
        }

        // Persistir los cambios en lote
        ingredientPersistencePort.updateIngredientsStock(newAvailableStocks);

        // (Opcional) Registrar el motivo del ajuste, si se requiere un log o auditoría
        for (Map.Entry<UUID, Integer> newAvailableStock : newAvailableStocks.entrySet()) {
//            inventoryMovementServicePort.recordInventoryMovement(newAvailableStock.getKey(), -stockAdjustments.get(newAvailableStock.getKey()), reason, ingredient.getRestaurant().getUuid());
        }
//        ingredientPersistencePort.logStockAdjustment(ingredientUuids, reason);
    }

    @Override
    public Restaurant getRestaurantByIngredientUuid(UUID ingredientUuid) {
        return ingredientPersistencePort.getRestaurantByIngredientUuid(ingredientUuid).orElseThrow(() -> new RestaurantDoesNotExistException("..."));
    }
}
