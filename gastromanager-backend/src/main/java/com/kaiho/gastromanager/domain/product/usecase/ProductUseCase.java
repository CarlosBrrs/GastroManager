package com.kaiho.gastromanager.domain.product.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.product.api.ProductServicePort;
import com.kaiho.gastromanager.domain.product.exception.ProductAlreadyExistsException;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.domain.product.model.ProductIngredient;
import com.kaiho.gastromanager.domain.product.model.ProductRecipe;
import com.kaiho.gastromanager.domain.product.spi.ProductPersistencePort;
import com.kaiho.gastromanager.domain.recipe.api.RecipeServicePort;
import com.kaiho.gastromanager.domain.recipe.exception.RecipeDoesNotExistException;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {

    private final ProductPersistencePort productPersistencePort;
    private final RecipeServicePort recipeServicePort;
    private final IngredientServicePort ingredientServicePort;

    @Override
    public UUID createProduct(Product product) {
        if (productPersistencePort.productExistsByName(product.getName(), product.getRestaurant().getUuid())) {
            throw new ProductAlreadyExistsException(product.getName());
        }

        if (isAdvancedMode(product)) {
            validateReferences(product);
            product.setPurchasePrice(calculatePurchasePrice(product));
        } else if (isBasicMode(product) && product.getPurchasePrice() == null) {
            throw new IllegalArgumentException("Purchase price is required for basic mode products");
        }


        return productPersistencePort.createProduct(product);
    }

    @Override
    public Page<Product> getAllProducts(ProductSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return productPersistencePort.getAllProducts(criteria, pageable);
    }

    @Override
    public List<Product> findAllByUuidInAndRestaurant(List<UUID> productUuids, UUID restaurantUuid) {
        return productPersistencePort.findAllByUuidInAndRestaurant(productUuids, restaurantUuid);
    }

    @Override
    public ProductGroupByResult getProductsGroupedByCategory(ProductGroupByCriteria criteria) {
        UUID restaurantUuid = getCurrentRestaurant();
        return productPersistencePort.getProductsGroupedByCategory(criteria, restaurantUuid);
    }

    /**
     * Calcula el precio de compra sumando el costo de las recetas e ingredientes.
     */
    private BigDecimal calculatePurchasePrice(Product product) {
        BigDecimal totalCost = BigDecimal.ZERO;

        // Sumar costo de recetas
        if (product.getRecipes() != null && !product.getRecipes().isEmpty()) {
            for (ProductRecipe productRecipe : product.getRecipes()) {
                // Obtener la receta completa con su costo
                Recipe recipe = recipeServicePort.getRecipeById(productRecipe.getRecipe().getUuid());

                // Multiplicar el costo de la receta por el multiplicador de cantidad
                BigDecimal recipeCost = recipe.getCost().multiply(BigDecimal.valueOf(productRecipe.getQuantityMultiplier()));
                totalCost = totalCost.add(recipeCost);

                // Actualizar la receta en el producto con la información completa
//                productRecipe.setRecipe(recipe);
            }
        }

        // Sumar costo de ingredientes directos
        if (product.getIngredients() != null && !product.getIngredients().isEmpty()) {
            for (ProductIngredient productIngredient : product.getIngredients()) {
                // Obtener el ingrediente completo con su precio
                Ingredient ingredient = ingredientServicePort.getIngredientById(productIngredient.getIngredient().getUuid());

                // Calcular el costo: (precioUnitario * cantidad) / 1000 si es en gramos
                // El precio unitario está en el precio por unidad base del ingrediente
                BigDecimal ingredientCost = ingredient.getPricePerUnit()
                                                      .multiply(BigDecimal.valueOf(productIngredient.getQuantity()));
//                        .divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);

                totalCost = totalCost.add(ingredientCost);

                // Actualizar el ingrediente en el producto con la información completa
//                productIngredient.setIngredient(ingredient);
            }
        }

        return totalCost;
    }

    /**
     * Determina si el producto está en modo avanzado.
     */
    private boolean isAdvancedMode(Product product) {
        return (product.getRecipes() != null && !product.getRecipes().isEmpty()) ||
                (product.getIngredients() != null && !product.getIngredients().isEmpty());
    }

    /**
     * Determina si el producto está en modo básico.
     */
    private boolean isBasicMode(Product product) {
        return !isAdvancedMode(product);
    }

    private void validateReferences(Product product) {

        if (product.getRecipes() != null) {
            product.getRecipes().forEach(pr -> {
                if (!recipeServicePort.existsByUuid(pr.getRecipe().getUuid())) {
                    throw new RecipeDoesNotExistException(pr.getRecipe().getUuid());
                }
            });
        }

        if (product.getIngredients() != null) {
            product.getIngredients().forEach(pi -> {
                if (!ingredientServicePort.existsByUuid(pi.getIngredient().getUuid())) {
                    throw new IngredientDoesNotExistException(pi.getIngredient().getUuid().toString());
                }
            });
        }

    }
}
