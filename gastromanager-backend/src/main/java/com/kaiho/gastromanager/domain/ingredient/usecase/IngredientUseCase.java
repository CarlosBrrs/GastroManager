package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistExceptionException;
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
        return ingredientPersistencePort.getIngredientByUuid(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistExceptionException(uuid.toString()));
    }

    @Override
    @Transactional
    public UUID addIngredient(Ingredient ingredient) {
        return ingredientPersistencePort.addIngredient(ingredient);
    }

    @Override
    @Transactional
    public Ingredient updateIngredient(UUID uuid, Ingredient ingredient) {

        Ingredient ingredientById = ingredientPersistencePort.getIngredientByUuid(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistExceptionException(uuid.toString()));

        // TODO: Move this to adapter, here we only validate business rules and in adapter we retrieve obj to update
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
