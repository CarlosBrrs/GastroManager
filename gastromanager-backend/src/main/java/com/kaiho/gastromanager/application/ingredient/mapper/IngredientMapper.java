package com.kaiho.gastromanager.application.ingredient.mapper;

import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
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
}
