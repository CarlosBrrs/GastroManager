package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductItemIngredientEntityMapper {

    public ProductItemIngredient toDomain(ProductItemIngredientEntity productItemIngredientEntity) {
        if (productItemIngredientEntity == null) {
            return null;
        }
        return ProductItemIngredient.builder()
                .ingredientUuid(productItemIngredientEntity.getIngredient().getUuid())
                .quantity(productItemIngredientEntity.getQuantity())
                .unit(productItemIngredientEntity.getUnit())
                .build();
    }

    public ProductItemIngredientEntity toEntity(ProductItemIngredient productItemIngredient, IngredientEntity ingredientEntity) {
        if (productItemIngredient == null || ingredientEntity == null) {
            return null;
        }
        return ProductItemIngredientEntity.builder()
                .ingredient(ingredientEntity)
                .quantity(productItemIngredient.quantity())
                .unit(productItemIngredient.unit())
                .build();
    }
}
