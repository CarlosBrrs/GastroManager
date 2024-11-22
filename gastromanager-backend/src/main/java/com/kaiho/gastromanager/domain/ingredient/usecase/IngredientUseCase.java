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
    public Ingredient getIngredientById(UUID uuid) {
        return ingredientPersistencePort.getIngredientByUuid(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
    }

    @Override
    @Transactional
    public UUID addIngredient(Ingredient ingredient) {
        if (ingredientPersistencePort.ingredientExistsByName(ingredient.name())) {
            throw new IngredientAlreadyExistsException(ingredient.name());
        }
        validateStockQuantities(ingredient.minimumStockQuantity(), ingredient.availableStock());
        UUID ingredientUuid = ingredientPersistencePort.addIngredient(ingredient);
        inventoryMovementServicePort.recordInventoryMovement(ingredientUuid, ingredient.availableStock(), "Initial stock");
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

        Ingredient ingredientById = ingredientPersistencePort.getIngredientByUuid(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
        if (!ingredientById.name().equals(ingredient.name()) &&
                ingredientPersistencePort.ingredientExistsByName(ingredient.name())) {
            throw new IngredientAlreadyExistsException(ingredient.name());
        }
        validateStockQuantities(ingredient.minimumStockQuantity(), ingredientById.availableStock());
        return ingredientPersistencePort.updateIngredient(uuid, ingredient);
    }
    @Override
    @Transactional
    public UUID adjustIngredientStock(UUID ingredientUuid, int newStock, String reason) {
        Ingredient ingredientToUpdate = ingredientPersistencePort.getIngredientByUuid(ingredientUuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));

        if (newStock == ingredientToUpdate.availableStock()) {
            throw new AvailableStockNotUpdatedException();
        }
//        validateStockQuantities(ingredientToUpdate.minimumStockQuantity(), newStock);
        UUID updatedIngredientUuid = ingredientPersistencePort.updateIngredientStock(ingredientUuid, newStock);

        // Registrar la diferencia de stock en caso de que haya un cambio en el inventario
        int stockDifference = newStock - ingredientToUpdate.availableStock();
//        String reason = stockDifference > 0 ? "Adjust of stock for increment" : "Adjust of stock for reduction";
        inventoryMovementServicePort.recordInventoryMovement(updatedIngredientUuid, stockDifference, reason);
        return updatedIngredientUuid;
    }

    @Override
    public Map<UUID, Ingredient> getIngredientsByUuids(Set<UUID> uuids) {
        // Consulta los ingredientes desde el puerto de persistencia
        List<Ingredient> ingredients = ingredientPersistencePort.findIngredientsByUuids(uuids);

        // Convertir la lista de ingredientes en un mapa con el UUID como clave
        return ingredients.stream()
                .collect(Collectors.toMap(Ingredient::uuid, ingredient -> ingredient));
    }

    @Override
    public void batchAdjustStock(Map<UUID, Integer> stockAdjustments, String reason) {
        // Obtener los ingredientes afectados
        Set<UUID> ingredientUuids = stockAdjustments.keySet();
        List<Ingredient> ingredients = ingredientPersistencePort.findIngredientsByUuids(ingredientUuids);

        Map<UUID, Integer> newAvailableStocks = new HashMap<>();

        // Actualizar el stock de cada ingrediente
        for (Ingredient ingredient : ingredients) {
            Integer adjustment = stockAdjustments.get(ingredient.uuid());
            int newStock = (ingredient.availableStock() - adjustment);

            if (newStock < 0) {
                throw new InsufficientStockException(ingredient.name(), adjustment, ingredient.availableStock());
            }
            newAvailableStocks.put(ingredient.uuid(), newStock);
        }

        // Persistir los cambios en lote
        ingredientPersistencePort.updateIngredientsStock(newAvailableStocks);

        // (Opcional) Registrar el motivo del ajuste, si se requiere un log o auditoría
        for (Map.Entry<UUID, Integer> newAvailableStock: newAvailableStocks.entrySet()) {
            inventoryMovementServicePort.recordInventoryMovement(newAvailableStock.getKey(), -stockAdjustments.get(newAvailableStock.getKey()), reason);
        }
//        ingredientPersistencePort.logStockAdjustment(ingredientUuids, reason);
    }
}
