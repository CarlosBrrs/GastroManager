package com.kaiho.gastromanager.application.productitemingredient.mapper;

import com.kaiho.gastromanager.application.productitemingredient.dto.request.ProductItemIngredientRequestDto;
import com.kaiho.gastromanager.application.productitemingredient.dto.response.ProductItemIngredientResponseDto;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import org.springframework.stereotype.Component;

@Component
public class ProductItemIngredientMapper {

    public ProductItemIngredient toDomain(ProductItemIngredientRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null) {
            return null;
        }

        return ProductItemIngredient.builder()
                .ingredientUuid(ingredientRequestDto.ingredientUuid())
                .quantity(ingredientRequestDto.quantity())
                .unit(Unit.valueOf(ingredientRequestDto.unit()))
                .build();
    }

    public ProductItemIngredientResponseDto toResponse(ProductItemIngredient productItemIngredient) {
        if (productItemIngredient == null) {
            return null;
        }
        return ProductItemIngredientResponseDto.builder()
                .ingredientUuid(productItemIngredient.ingredientUuid())
                .quantity(productItemIngredient.quantity())
                .unit(productItemIngredient.unit())
                .build();
    }
}
