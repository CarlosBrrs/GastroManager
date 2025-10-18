package com.kaiho.gastromanager.infrastructure.product.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductIngredient;
import com.kaiho.gastromanager.domain.product.model.ProductRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductIngredientEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductRecipeEntity;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class ProductEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(getCurrentRestaurant())
                                                                      .orElseThrow(() -> new IllegalArgumentException("Restaurant does not exist"));

        ProductEntity entity = ProductEntity.builder()
                                            .uuid(product.getUuid())
                                            .name(product.getName())
                                            .description(product.getDescription())
                                            .category(product.getCategory())
                                            .salePrice(product.getSalePrice())
                                            .purchasePrice(product.getPurchasePrice())
                                            .isEnabled(product.isEnabled())
                                            .restaurant(restaurantEntity)
                                            .mode(product.getMode())
                                            .recipes(new ArrayList<>())
                                            .ingredients(new ArrayList<>())
                                            .build();

        // Mapear recetas si existen
        if (product.getRecipes() != null && !product.getRecipes().isEmpty()) {
            for (ProductRecipe productRecipe : product.getRecipes()) {
                ProductRecipeEntity recipeEntity = ProductRecipeEntity.builder()
                                                                      .product(entity)
                                                                      .recipe(RecipeEntity.builder().uuid(productRecipe.getRecipe().getUuid()).build())
                                                                      .quantityMultiplier(productRecipe.getQuantityMultiplier())
                                                                      .build();
                entity.addRecipe(recipeEntity);
            }
        }

        // Mapear ingredientes si existen
        if (product.getIngredients() != null && !product.getIngredients().isEmpty()) {
            for (ProductIngredient productIngredient : product.getIngredients()) {
                ProductIngredientEntity ingredientEntity = ProductIngredientEntity.builder()
                                                                                  .product(entity)
                                                                                  .ingredient(IngredientEntity.builder().uuid(productIngredient.getIngredient().getUuid()).build())
                                                                                  .quantity(productIngredient.getQuantity())
                                                                                  .build();
                entity.addIngredient(ingredientEntity);
            }
        }

        restaurantEntity.addProduct(entity);
        return entity;
    }

    public Product toDomain(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        // Mapear recetas si existen
        List<ProductRecipe> productRecipes = null;
        if (productEntity.getRecipes() != null && !productEntity.getRecipes().isEmpty()) {
            productRecipes = productEntity.getRecipes().stream()
                                          .map(recipeEntity -> ProductRecipe.builder()
                                                                            .recipe(Recipe.builder().uuid(recipeEntity.getRecipe().getUuid()).build())
                                                                            .quantityMultiplier(recipeEntity.getQuantityMultiplier())
                                                                            .build())
                                          .toList();
        }

        // Mapear ingredientes si existen
        List<ProductIngredient> productIngredients = null;
        if (productEntity.getIngredients() != null && !productEntity.getIngredients().isEmpty()) {
            productIngredients = productEntity.getIngredients().stream()
                                              .map(ingredientEntity -> ProductIngredient.builder()
                                                                                        .ingredient(Ingredient.builder().uuid(ingredientEntity.getIngredient().getUuid()).build())
                                                                                        .quantity(ingredientEntity.getQuantity())
                                                                                        .build())
                                              .toList();
        }

        return Product.builder()
                      .uuid(productEntity.getUuid())
                      .name(productEntity.getName())
                      .description(productEntity.getDescription())
                      .category(productEntity.getCategory())
                      .salePrice(productEntity.getSalePrice())
                      .purchasePrice(productEntity.getPurchasePrice())
                      .isEnabled(productEntity.getIsEnabled())
                      .recipes(productRecipes)
                      .ingredients(productIngredients)
                      .build();
    }
}
