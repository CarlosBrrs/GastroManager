package com.kaiho.gastromanager.domain.recipe.usecase;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.recipe.api.RecipeServicePort;
import com.kaiho.gastromanager.domain.recipe.exception.RecipeDoesNotExistException;
import com.kaiho.gastromanager.domain.recipe.model.BaseRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.recipe.spi.RecipePersistencePort;
import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class RecipeUseCase implements RecipeServicePort {

    private final RecipePersistencePort recipePersistencePort;

    @Transactional
    @Override
    public Recipe createRecipe(Recipe recipe) {

        if (recipe.getBaseRecipe() != null) {
            baseRecipeValidations(recipe);

        }

        Set<UUID> seen = new HashSet<>();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            if (!seen.add(ing.getIngredient().getUuid())) {
                throw new IllegalArgumentException("Ingrediente duplicado: " + ing.getIngredient().getName());
            }
        }
        calculateCost(recipe);
        return recipePersistencePort.createRecipe(recipe);

    }

    @Override
    public Page<Recipe> getAllRecipes(RecipeSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return recipePersistencePort.getAllRecipes(criteria, pageable);
    }

    @Override
    public Recipe getRecipeById(UUID recipeUuid) {
        return recipePersistencePort.getRecipeByUuid(recipeUuid)
                                    .orElseThrow(() -> new RecipeDoesNotExistException(recipeUuid));
    }

    private void calculateCost(Recipe recipe) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (RecipeIngredient ing : recipe.getIngredients()) {
            Ingredient fullIng = ing.getIngredient();
            BigDecimal cost = BigDecimal.valueOf(fullIng.getPricePerUnit()).multiply(BigDecimal.valueOf(ing.getQuantity()));
            totalCost = totalCost.add(cost);
        }
        if (recipe.getBaseRecipe() != null) {
            totalCost = totalCost.add(recipe.getBaseRecipe().getRecipe().getCost());
        }
        recipe.setCost(totalCost);

    }

    private void baseRecipeValidations(Recipe recipe) {

        baseRecipePortionValidation(recipe.getBaseRecipe());
        inheritanceValidations(recipe);

        collisionIngredientValidation(recipe);
    }

    private void baseRecipePortionValidation(BaseRecipe baseRecipe) {
        if (baseRecipe.getPortion() == null || baseRecipe.getPortion() <= 0) {
            throw new IllegalArgumentException("La porción de la receta base no puede ser nula o menor a 1");
        }

    }

    private void collisionIngredientValidation(Recipe recipe) {
        Set<UUID> baseRecipeIngredientUuids = getUuidsFromRecipeIngredientList(recipe.getBaseRecipe().getRecipe().getIngredients());
        Set<UUID> ingredientUuids = getUuidsFromRecipeIngredientList(recipe.getIngredients());

        Set<UUID> repeated = new HashSet<>(baseRecipeIngredientUuids);
        repeated.retainAll(ingredientUuids);

        if (!repeated.isEmpty()) {
            throw new IllegalArgumentException("Recipe overrides base recipe ingredients: " + repeated);
        }
    }

    private void inheritanceValidations(Recipe recipe) {
        UUID baseId = recipe.getBaseRecipe().getRecipe().getUuid();
        if (baseId.equals(recipe.getUuid())) {
            throw new IllegalArgumentException("Una receta no puede heredar de sí misma");
        }

        if (recipe.getBaseRecipe().getRecipe().getBaseRecipe() != null) {
            throw new IllegalArgumentException("No se permite heredar de una receta que ya hereda de otra");
        }
    }

    private Set<UUID> getUuidsFromRecipeIngredientList(List<RecipeIngredient> list) {
        return list.stream()
                   .map(ing -> ing.getIngredient().getUuid())
                   .collect(Collectors.toSet());
    }

}
