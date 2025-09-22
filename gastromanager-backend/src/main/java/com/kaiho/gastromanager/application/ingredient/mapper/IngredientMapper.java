package com.kaiho.gastromanager.application.ingredient.mapper;

import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientUpdateRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientDetailResponseDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientSummaryResponseDto;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientSummaryResponseDto toResponseSummary(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return IngredientSummaryResponseDto.builder()
                                           .uuid(ingredient.getUuid())
                                           .name(ingredient.getName())
                                           .supplier(ingredient.getSupplier())
                                           .minimumStockQuantity(ingredient.getMinimumStockQuantity())
                                           .unit(ingredient.getUnit().getSymbol())
                                           .pricePerUnit(ingredient.getPricePerUnit())
                                           .availableStock(ingredient.getAvailableStock())
                                           .updatedDate(ingredient.getUpdatedDate())
                                           .updatedBy(ingredient.getUpdatedBy())
                                           .createdBy(ingredient.getCreatedBy())
                                           .createdDate(ingredient.getCreatedDate())
                                           .build();
    }

    public IngredientDetailResponseDto toResponseDetail(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return IngredientDetailResponseDto.builder()
                                          .uuid(ingredient.getUuid())
                                          .name(ingredient.getName())
                                          .supplier(ingredient.getSupplier())
                                          .minimumStockQuantity(ingredient.getMinimumStockQuantity())
                                          .unit(ingredient.getUnit().getSymbol())
                                          .pricePerUnit(ingredient.getPricePerUnit())
                                          .availableStock(ingredient.getAvailableStock())
                                          .updatedDate(ingredient.getUpdatedDate())
                                          .updatedBy(ingredient.getUpdatedBy())
                                          .createdBy(ingredient.getCreatedBy())
                                          .createdDate(ingredient.getCreatedDate())
                                          .build();
    }

    public Ingredient toDomain(IngredientRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null) {
            return null;
        }
        return Ingredient.builder()
                         .name(ingredientRequestDto.name())
                         .availableStock(ingredientRequestDto.availableStock())
                         .unit(Unit.valueOf(ingredientRequestDto.unit()))
                         .pricePerUnit(ingredientRequestDto.pricePerUnit())
                         .minimumStockQuantity(ingredientRequestDto.minimumStockQuantity())
                         .supplier(ingredientRequestDto.supplier())
                         .build();
    }

    public Ingredient toDomain(IngredientUpdateRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null) {
            return null;
        }
        return Ingredient.builder()
                         .name(ingredientRequestDto.name())
                         .unit(Unit.valueOf(ingredientRequestDto.unit()))
                         .pricePerUnit(ingredientRequestDto.pricePerUnit())
                         .minimumStockQuantity(ingredientRequestDto.minimumStockQuantity())
                         .supplier(ingredientRequestDto.supplier())
                         .build();
    }

}
