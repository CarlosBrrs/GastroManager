package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
@Entity
@Table(name = "product_item_ingredients")
@Setter
public class ProductItemIngredientEntity extends Auditable implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_uuid", nullable = false)
    private ProductItemEntity productItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_uuid", nullable = false)
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private double quantity; // Quantity of the ingredient for the dish

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit; // Define an Enum for units (GRAMS, UNITS, MILLILITRES)
}