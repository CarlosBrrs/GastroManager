package com.kaiho.gastromanager.application.ingredient.dto.request;

import lombok.Builder;

@Builder
public record UpdateIngredientRequestDto(String name,
                                         String unit,
                                         String supplier,
                                         double pricePerUnit,
                                         int minimumStockQuantity) {
}
