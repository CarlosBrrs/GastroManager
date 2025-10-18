package com.kaiho.gastromanager.application.product.mapper;

import com.kaiho.gastromanager.application.product.dto.request.ProductRequestDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductSummaryResponseDto;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductIngredient;
import com.kaiho.gastromanager.domain.product.model.ProductMode;
import com.kaiho.gastromanager.domain.product.model.ProductRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
public class ProductMapper {

    public Product toDomain(ProductRequestDto productRequestDto) {
        if (productRequestDto == null) {
            return null;
        }

        // Mapear recetas si existen
        List<ProductRecipe> productRecipes = null;
        if (productRequestDto.recipes() != null && !productRequestDto.recipes().isEmpty()) {
            productRecipes = productRequestDto.recipes().stream()
                                              .map(recipeDto -> ProductRecipe.builder()
                                                                             .recipe(Recipe.builder().uuid(recipeDto.recipeUuid()).build())
                                                                             .quantityMultiplier(recipeDto.quantityMultiplier())
                                                                             .build())
                                              .toList();
        }

        // Mapear ingredientes si existen
        List<ProductIngredient> productIngredients = null;
        if (productRequestDto.ingredients() != null && !productRequestDto.ingredients().isEmpty()) {
            productIngredients = productRequestDto.ingredients().stream()
                                                  .map(ingredientDto -> ProductIngredient.builder()
                                                                                         .ingredient(Ingredient.builder().uuid(ingredientDto.ingredientUuid()).build())
                                                                                         .quantity(ingredientDto.quantity())
                                                                                         .build())
                                                  .toList();
        }

        BigDecimal purchasePrice = null;
        ProductMode mode = ProductMode.ADVANCED;
        if (productRequestDto.isBasicMode() && productRequestDto.purchasePrice() != null) {
            purchasePrice = BigDecimal.valueOf(productRequestDto.purchasePrice());
            mode = ProductMode.BASIC;
        }

        return Product.builder()
                      .name(productRequestDto.name())
                      .description(productRequestDto.description())
                      .category(productRequestDto.category())
                      .salePrice(BigDecimal.valueOf(productRequestDto.salePrice()))
                      .purchasePrice(purchasePrice)
                      .restaurant(Restaurant.builder().uuid(getCurrentRestaurant()).build())
                      .isEnabled(true)
                      .mode(mode)
                      .recipes(productRecipes)
                      .ingredients(productIngredients)
                      .build();
    }

    public ProductSummaryResponseDto toResponseSummary(Product product) {
        if (product == null) {
            return null;
        }
        return ProductSummaryResponseDto.builder()
                                        .uuid(product.getUuid())
                                        .name(product.getName())
                                        .description(product.getDescription())
                                        .purchasePrice(product.getPurchasePrice().doubleValue())
                                        .salePrice(product.getSalePrice().doubleValue())
                                        .build();
    }
}
