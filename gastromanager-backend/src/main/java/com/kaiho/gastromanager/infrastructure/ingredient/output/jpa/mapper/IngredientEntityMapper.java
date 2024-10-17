package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.stereotype.Component;

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
                .uuid(ingredient.uuid())
                .name(ingredient.name())
                .availableStock(ingredient.availableStock())
                .unit(ingredient.unit())
                .pricePerUnit(ingredient.pricePerUnit())
                .minimumStockQuantity(ingredient.minimumStockQuantity())
                .supplier(ingredient.supplier())
                .createdDate(ingredient.createdDate())
                .createdBy(ingredient.createdBy())
                .build();
    }
}
