package com.kaiho.gastromanager.application.ingredient.dto.request;

import jakarta.validation.constraints.Positive;

public record AdjustStockRequestDto(
        @Positive(message = "New stock has to be more than 0")
        int newStock) {
}
