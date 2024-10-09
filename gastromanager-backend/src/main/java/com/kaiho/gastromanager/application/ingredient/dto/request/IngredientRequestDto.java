package com.kaiho.gastromanager.application.ingredient.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IngredientRequestDto {

    private String name;
    private int stockLevel;
    private String unit;
    private double pricePerUnit;
    private int minimumStockLevel;
    private String updateReason;
}
