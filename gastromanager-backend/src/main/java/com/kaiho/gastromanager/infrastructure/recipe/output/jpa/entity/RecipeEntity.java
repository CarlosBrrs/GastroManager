package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.entity.RecipeIngredientEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RecipeEntity extends Auditable implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_uuid", nullable = false)
    private RestaurantEntity restaurant;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_recipe_uuid")
    private RecipeEntity baseRecipe;

    @Column(name = "base_recipe_portion")
    private Double baseRecipePortion;

    @OneToMany(mappedBy = "baseRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeEntity> derivedRecipes = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientEntity> ingredients = new ArrayList<>();

    public void setBaseRecipe(RecipeEntity baseRecipe) {
        this.baseRecipe = baseRecipe;
        if (baseRecipe != null && !baseRecipe.getDerivedRecipes().contains(this)) {
            baseRecipe.getDerivedRecipes().add(this);
        }
    }

    public void removeBaseRecipe() {
        if (this.baseRecipe != null) {
            this.baseRecipe.getDerivedRecipes().remove(this);
            this.baseRecipe = null;
        }
    }

    public void addIngredient(RecipeIngredientEntity ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public void removeIngredient(RecipeIngredientEntity ingredient) {
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
    }
}
