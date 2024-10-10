package com.kaiho.gastromanager.application.ingredient.mapper;

import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import org.springframework.stereotype.Service;

@Service
public class IngredientMapper {

    public IngredientResponseDto toResponse(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return IngredientResponseDto.builder()
                .uuid(ingredient.uuid())
                .name(ingredient.name())
                .unit(ingredient.unit().getSymbol())
                .pricePerUnit(ingredient.pricePerUnit())
                .stockLevel(ingredient.stockLevel())
                .lastUpdated(ingredient.lastUpdated())
                .build();
    }

    public Ingredient toDomain(IngredientRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null) {
            return null;
        }
        return Ingredient.builder()
                .name(ingredientRequestDto.getName())
                .stockLevel(ingredientRequestDto.getStockLevel())
                .unit(Unit.valueOf(ingredientRequestDto.getUnit()))
                .pricePerUnit(ingredientRequestDto.getPricePerUnit())
                .minimumStockLevel(ingredientRequestDto.getMinimumStockLevel())
                .updateReason(ingredientRequestDto.getUpdateReason()).build();
    }
}
