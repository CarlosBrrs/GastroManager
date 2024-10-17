package com.kaiho.gastromanager.application.ingredient.dto.request;

import lombok.Builder;

@Builder
public record IngredientRequestDto(String name, int availableStock, String unit, String supplier, double pricePerUnit,
                                   int minimumStockQuantity) {

}
