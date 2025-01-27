package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class IngredientEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;

    public Ingredient toDomain(IngredientEntity ingredientEntity) {
        if (ingredientEntity == null) {
            return null;
        }
        return Ingredient.builder()
                .uuid(ingredientEntity.getUuid())
                .name(ingredientEntity.getName())
                .availableStock(ingredientEntity.getAvailableStock())
                .unit(ingredientEntity.getUnit())
                .pricePerUnit(ingredientEntity.getPricePerUnit())
                .supplier(ingredientEntity.getSupplier())
                .minimumStockQuantity(ingredientEntity.getMinimumStockQuantity())
                .createdBy(ingredientEntity.getCreatedBy())
                .createdDate(ingredientEntity.getCreatedDate())
                .updatedBy(ingredientEntity.getUpdatedBy())
                .updatedDate(ingredientEntity.getUpdatedDate())
                .build();
    }

    public IngredientEntity toEntity(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(ingredient.getRestaurant().getUuid()).orElseThrow(() -> new RestaurantDoesNotExistException(ingredient.getRestaurant().getUuid().toString()));
        return IngredientEntity.builder()
                .uuid(ingredient.getUuid())
                .name(ingredient.getName())
                .availableStock(ingredient.getAvailableStock())
                .unit(ingredient.getUnit())
                .restaurant(restaurantEntity)
                .pricePerUnit(ingredient.getPricePerUnit())
                .inventoryMovements(new ArrayList<>())
                .minimumStockQuantity(ingredient.getMinimumStockQuantity())
                .supplier(ingredient.getSupplier())
                .createdDate(ingredient.getCreatedDate())
                .createdBy(ingredient.getCreatedBy())
                .build();
    }
}
