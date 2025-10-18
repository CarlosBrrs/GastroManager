package com.kaiho.gastromanager.domain.product.model;

import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Representa una receta utilizada en un producto.
 * Contiene la receta y el multiplicador de cantidad que indica cuántas porciones se usan.
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductRecipe {
    private Recipe recipe;
    private Double quantityMultiplier;
}

