package com.kaiho.gastromanager.application.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * DTO para representar una receta dentro de un producto.
 * Contiene el UUID de la receta y el multiplicador de cantidad.
 */
public record ProductRecipeRequestDto(
        @NotNull(message = "Recipe UUID is required")
        UUID recipeUuid,

        @NotNull(message = "Quantity multiplier is required")
        @Positive(message = "Quantity multiplier must be positive")
        Double quantityMultiplier
) {
}

