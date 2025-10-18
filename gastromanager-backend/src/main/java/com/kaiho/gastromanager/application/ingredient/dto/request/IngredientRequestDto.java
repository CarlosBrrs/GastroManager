package com.kaiho.gastromanager.application.ingredient.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record IngredientRequestDto(String name,
                                   @Positive(message = "Available stock has to be more than 0")
                                   int availableStock,
                                   String unit, String supplier, BigDecimal pricePerUnit,
                                   int minimumStockQuantity) {

}
