package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IngredientUseCase implements IngredientServicePort {

    private final IngredientPersistencePort ingredientPersistencePort;
    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientPersistencePort.getAllIngredients();
    }

    @Override
    public Ingredient getIngredientById(UUID uuid) {
        return ingredientPersistencePort.getIngredientById(uuid)
                .orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));
    }

    @Override
    public UUID addIngredient(Ingredient ingredient) {
        return ingredientPersistencePort.createIngredient(ingredient);
    }

}
