package com.kaiho.gastromanager.domain.productitem.model;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public final class ProductItem {
    private final UUID uuid;
    private final String createdBy;
    private final Instant createdDate;
    private final Restaurant restaurant;
    private String name;
    private String description;
    private double price;
    private Category category;
    private boolean isEnabled;
    private List<ProductItemIngredient> ingredients;
    private String updatedBy;
    private Instant updatedDate;

}
