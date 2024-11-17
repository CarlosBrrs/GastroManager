package com.kaiho.gastromanager.domain.productitemingredient.api;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;

import java.util.List;
import java.util.UUID;

public interface ProductItemIngredientServicePort {

    List<ProductItemIngredient> getByProductItemUuids(List<UUID> productItemUuids);
}
