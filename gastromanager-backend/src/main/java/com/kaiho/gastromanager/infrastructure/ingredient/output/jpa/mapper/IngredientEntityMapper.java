package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.stereotype.Service;

@Service
public class IngredientEntityMapper {

    public Ingredient toDomain(IngredientEntity ingredientEntity) {
        if (ingredientEntity == null) {
            return null;
        }
        return Ingredient.builder()
                .uuid(ingredientEntity.getUuid())
                .name(ingredientEntity.getName())
                .stockLevel(ingredientEntity.getStockLevel())
                .unit(ingredientEntity.getUnit())
                .pricePerUnit(ingredientEntity.getPricePerUnit())
                .createdBy(ingredientEntity.getCreatedBy())
                .createdDateTime(ingredientEntity.getCreatedDateTime())
                .updatedBy(ingredientEntity.getUpdatedBy())
                .lastUpdated(ingredientEntity.getLastUpdated())
                .build();
    }
}
