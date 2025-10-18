package com.kaiho.gastromanager.application.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * DTO para representar un ingrediente dentro de un producto.
 * Contiene el UUID del ingrediente y la cantidad utilizada.
 */
public record ProductIngredientRequestDto(
        @NotNull(message = "Ingredient UUID is required")
        UUID ingredientUuid,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Double quantity
) {
}

