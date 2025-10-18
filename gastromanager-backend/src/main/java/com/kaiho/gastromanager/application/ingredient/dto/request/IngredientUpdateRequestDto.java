package com.kaiho.gastromanager.application.ingredient.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record IngredientUpdateRequestDto(String name,
                                         String unit,
                                         String supplier,
                                         BigDecimal pricePerUnit,
                                         int minimumStockQuantity) {
}
