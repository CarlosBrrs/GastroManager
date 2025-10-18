package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.entity.InventoryMovementEntity;
import com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.entity.RecipeIngredientEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ingredients")
@FilterDef(name = "restaurantFilter", parameters = @ParamDef(name = "restaurantUuid", type = UUID.class))
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class IngredientEntity extends Auditable implements Serializable {

    @Column(nullable = false)
    private String name;

    private double availableStock;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InventoryMovementEntity> inventoryMovements = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    private BigDecimal pricePerUnit;

    private int minimumStockQuantity;
    private String supplier;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid")
    private RestaurantEntity restaurant;

    public void addInventoryMovement(InventoryMovementEntity inventoryMovement) {

        this.inventoryMovements.add(inventoryMovement);
        inventoryMovement.setIngredient(this);
    }
}
