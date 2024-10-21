package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "ingredients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class IngredientEntity extends Auditable implements Serializable {

    @Column(nullable = false)
    private String name;

    private int availableStock;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private double pricePerUnit;

    private int minimumStockQuantity;
    private String supplier;
}
