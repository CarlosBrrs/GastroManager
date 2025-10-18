package com.kaiho.gastromanager.infrastructure.product.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
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
import org.hibernate.annotations.Filter;

import java.io.Serializable;

/**
 * Entidad que representa la relación entre un producto y un ingrediente directo.
 * Indica qué ingredientes usa directamente un producto (sin pasar por una receta).
 */
@Entity
@Table(name = "product_ingredients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_uuid", "ingredient_uuid"}))
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductIngredientEntity extends Auditable implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_uuid", nullable = false)
    private ProductEntity product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_uuid", nullable = false)
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private Double quantity;
}

