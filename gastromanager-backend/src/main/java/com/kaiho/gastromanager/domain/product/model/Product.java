package com.kaiho.gastromanager.domain.product.model;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Product {
    private final UUID uuid;
    private final String name;
    private final String description;
    private final String category;
    private BigDecimal salePrice;
    private BigDecimal purchasePrice;
    private final boolean isEnabled;
    private final String createdBy;
    private final Instant createdDate;
    private final String updatedBy;
    private final Instant updatedDate;
    private final Restaurant restaurant;
}
