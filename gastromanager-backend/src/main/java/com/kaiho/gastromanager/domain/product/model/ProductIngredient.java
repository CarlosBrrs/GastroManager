package com.kaiho.gastromanager.domain.product.model;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Representa un ingrediente utilizado directamente en un producto (sin receta intermedia).
 * Contiene el ingrediente y la cantidad utilizada.
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductIngredient {
    private Ingredient ingredient;
    private Double quantity;
}

