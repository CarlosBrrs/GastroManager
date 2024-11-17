package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.entity.InventoryMovementEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InventoryMovementEntity> inventoryMovements= new ArrayList<>();

    private double pricePerUnit;

    private int minimumStockQuantity;
    private String supplier;

    public void addInventoryMovement(InventoryMovementEntity inventoryMovement) {

        this.inventoryMovements.add(inventoryMovement);
        inventoryMovement.setIngredient(this);
    }
}
