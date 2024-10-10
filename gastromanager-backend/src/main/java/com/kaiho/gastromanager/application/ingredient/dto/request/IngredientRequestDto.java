package com.kaiho.gastromanager.application.ingredient.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IngredientRequestDto {

    private String name;
    private int stockLevel;
    private String unit;
    private double pricePerUnit;
    private int minimumStockLevel;
    private String updateReason;

}
