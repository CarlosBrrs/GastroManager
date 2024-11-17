package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.AvailableStockNotUpdatedException;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientAlreadyExistsException;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.exception.UnacceptableStockQuantitiesException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.domain.inventorymovement.api.InventoryMovementServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    public UUID adjustIngredientStock(UUID ingredientUuid, int newStock) {
        Ingredient ingredientToUpdate = ingredientPersistencePort.getIngredientByUuid(ingredientUuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));

        if (newStock == ingredientToUpdate.availableStock()) {
            throw new AvailableStockNotUpdatedException();
        }
        validateStockQuantities(ingredientToUpdate.minimumStockQuantity(), newStock);
        UUID updatedIngredientUuid = ingredientPersistencePort.updateIngredientStock(ingredientUuid, newStock);

        // Registrar la diferencia de stock en caso de que haya un cambio en el inventario
        int stockDifference = newStock - ingredientToUpdate.availableStock();
        String reason = stockDifference > 0 ? "Adjust of stock for increment" : "Adjust of stock for reduction";
        inventoryMovementServicePort.recordInventoryMovement(updatedIngredientUuid, stockDifference, reason);
        return updatedIngredientUuid;
    }
}
