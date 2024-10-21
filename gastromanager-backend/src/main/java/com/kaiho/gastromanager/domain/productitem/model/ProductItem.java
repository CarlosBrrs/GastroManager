package com.kaiho.gastromanager.domain.productitem.model;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ProductItem(UUID uuid,
                          String name,
                          String description,
                          double price,
                          Category category,
                          boolean isEnabled,
                          List<ProductItemIngredient> ingredients,
                          String createdBy,
                          Instant createdDate,
                          String updatedBy,
                          Instant updatedDate) {
}
