package com.kaiho.gastromanager.application.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTO para la creación de productos.
 * Soporta dos modos:
 * - BÁSICO: Solo requiere purchasePrice, sin recetas ni ingredientes
 * - AVANZADO: Requiere al menos una lista (recipes o ingredients) con elementos, purchasePrice se calcula automáticamente
 */
public record ProductRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        String category,

        @NotNull(message = "Sale price is required")
        @Positive(message = "Sale price must be positive")
        Double salePrice,

        Double purchasePrice,

        @Valid
        List<ProductRecipeRequestDto> recipes,

        @Valid
        List<ProductIngredientRequestDto> ingredients
) {
    /**
     * Determina si el producto está en modo avanzado.
     * Modo avanzado = tiene al menos una receta o ingrediente
     */
    public boolean isAdvancedMode() {
        return (recipes != null && !recipes.isEmpty()) ||
                (ingredients != null && !ingredients.isEmpty());
    }

    /**
     * Determina si el producto está en modo básico.
     * Modo básico = no tiene recetas ni ingredientes
     */
    public boolean isBasicMode() {
        return !isAdvancedMode();
    }
}
