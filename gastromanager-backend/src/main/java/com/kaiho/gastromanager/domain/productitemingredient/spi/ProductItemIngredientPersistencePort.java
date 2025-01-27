package com.kaiho.gastromanager.domain.productitemingredient.spi;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;

import java.util.List;
import java.util.UUID;

public interface ProductItemIngredientPersistencePort {
    List<ProductItemIngredient> findByProductItemUuids(List<UUID> productItemUuids, UUID restaurantUuid);
}
