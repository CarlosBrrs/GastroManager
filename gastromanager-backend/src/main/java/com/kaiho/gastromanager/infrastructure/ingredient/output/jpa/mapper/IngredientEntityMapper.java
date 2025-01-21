package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class IngredientEntityMapper {

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
        return IngredientEntity.builder()
                .uuid(ingredient.getUuid())
                .name(ingredient.getName())
                .availableStock(ingredient.getAvailableStock())
                .unit(ingredient.getUnit())
                .pricePerUnit(ingredient.getPricePerUnit())
                .inventoryMovements(new ArrayList<>())
                .minimumStockQuantity(ingredient.getMinimumStockQuantity())
                .supplier(ingredient.getSupplier())
                .createdDate(ingredient.getCreatedDate())
                .createdBy(ingredient.getCreatedBy())
                .build();
    }
}
