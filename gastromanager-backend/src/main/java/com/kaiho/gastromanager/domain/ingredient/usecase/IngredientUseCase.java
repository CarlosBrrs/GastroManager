package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IngredientUseCase implements IngredientServicePort {

    private final IngredientPersistencePort ingredientPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> getAllIngredients() {
        return ingredientPersistencePort.getAllIngredients();
    }

    @Override
    @Transactional(readOnly = true)
    public Ingredient getIngredientById(UUID uuid) {
        return ingredientPersistencePort.getIngredientById(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
    }

    @Override
    @Transactional
    public UUID addIngredient(Ingredient ingredient) {
        return ingredientPersistencePort.addIngredient(ingredient);
    }

    @Override
    @Transactional
    public Ingredient updateIngredient(UUID uuid, Ingredient ingredient) {

        // TODO: Solve the missed transactional call
        Ingredient ingredientById = getIngredientById(uuid);

        Ingredient ingredientToUpdate = Ingredient.builder()
                .uuid(ingredientById.uuid())
                .name(ingredient.name())
                .unit(ingredient.unit())
                .availableStock(ingredient.availableStock())
                .minimumStockQuantity(ingredient.minimumStockQuantity())
                .pricePerUnit(ingredient.pricePerUnit())
                .supplier(ingredient.supplier())
                .createdDate(ingredientById.createdDate())
                .createdBy(ingredientById.createdBy())
                .build();

        return ingredientPersistencePort.updateIngredient(ingredientToUpdate);
    }

}
