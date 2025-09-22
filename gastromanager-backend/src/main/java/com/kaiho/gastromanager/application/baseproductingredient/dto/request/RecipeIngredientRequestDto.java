package com.kaiho.gastromanager.application.baseproductingredient.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RecipeIngredientRequestDto(
        @NotNull(message = "El UUID del ingrediente es obligatorio")
        UUID ingredientUuid,
        @NotNull(message = "La cantidad es obligatoria")
        double quantity
) {
}
