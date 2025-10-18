package com.kaiho.gastromanager.domain.ingredient.model;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private final UUID uuid;
    private String name;
    private double availableStock;
    private Unit unit;
    private BigDecimal pricePerUnit;
    private String supplier;
    private int minimumStockQuantity;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
    private Restaurant restaurant;

}
