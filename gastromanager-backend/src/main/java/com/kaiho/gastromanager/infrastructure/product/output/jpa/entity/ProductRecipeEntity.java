package com.kaiho.gastromanager.infrastructure.product.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Entidad que representa la relación entre un producto y una receta.
 * Indica qué recetas usa un producto y con qué multiplicador de cantidad.
 */
@Entity
@Table(name = "product_recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_uuid", "recipe_uuid"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductRecipeEntity extends Auditable implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_uuid", nullable = false)
    private ProductEntity product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_uuid", nullable = false)
    private RecipeEntity recipe;

    @Column(name = "quantity_multiplier", nullable = false)
    private Double quantityMultiplier;
}

