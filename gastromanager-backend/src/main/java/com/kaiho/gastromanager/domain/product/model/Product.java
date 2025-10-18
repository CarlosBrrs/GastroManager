package com.kaiho.gastromanager.domain.product.model;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Representa un producto que se vende al cliente.
 * Un producto puede estar en dos modos:
 * - BÁSICO: Solo tiene precio de compra manual, sin recetas ni ingredientes
 * - AVANZADO: Tiene recetas y/o ingredientes, el precio de compra se calcula automáticamente
 */
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
    private final ProductMode mode;
    private final boolean isEnabled;
    private final String createdBy;
    private final Instant createdDate;
    private final String updatedBy;
    private final Instant updatedDate;
    private final Restaurant restaurant;

    // Listas para modo avanzado
    private List<ProductRecipe> recipes;
    private List<ProductIngredient> ingredients;
}
