package com.kaiho.gastromanager.application.productitemingredient.mapper;

import com.kaiho.gastromanager.application.productitemingredient.dto.request.ProductItemIngredientRequestDto;
import com.kaiho.gastromanager.application.productitemingredient.dto.response.ProductItemIngredientResponseDto;
import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductItemIngredientMapper {

    private final IngredientServicePort ingredientServicePort;

    public ProductItemIngredient toDomain(ProductItemIngredientRequestDto productItemIngredientRequestDto) {
        if (productItemIngredientRequestDto == null) {
            return null;
        }
        Ingredient ingredient = ingredientServicePort.getIngredientById(productItemIngredientRequestDto.ingredientUuid());

        return ProductItemIngredient.builder()
                                    .ingredient(ingredient)
                                    .quantity(productItemIngredientRequestDto.quantity())
                                    .build();
    }

    public ProductItemIngredientResponseDto toResponse(ProductItemIngredient productItemIngredient) {
        if (productItemIngredient == null) {
            return null;
        }
        return ProductItemIngredientResponseDto.builder()
                                               .ingredientUuid(productItemIngredient.getIngredient().getUuid())
                                               .unit(productItemIngredient.getIngredient().getUnit().getSymbol())
                                               .name(productItemIngredient.getIngredient().getName())
                                               .quantity(productItemIngredient.getQuantity())
                                               .build();
    }
}
