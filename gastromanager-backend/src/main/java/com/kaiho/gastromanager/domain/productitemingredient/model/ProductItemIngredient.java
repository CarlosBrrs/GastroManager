package com.kaiho.gastromanager.domain.productitemingredient.model;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class ProductItemIngredient {
    private final UUID uuid;
    private final double quantity;
    private ProductItem productItem;
    private Ingredient ingredient;

}
