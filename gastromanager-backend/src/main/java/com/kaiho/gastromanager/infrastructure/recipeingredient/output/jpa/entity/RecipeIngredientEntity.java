package com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
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

@Entity
@Table(name = "recipes_ingredients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_uuid", "ingredient_uuid"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RecipeIngredientEntity extends Auditable implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_uuid", nullable = false)
    private RecipeEntity recipe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_uuid", nullable = false)
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private double quantity;

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
        if (!recipe.getIngredients().contains(this)) {
            recipe.getIngredients().add(this);
        }
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
        if (!ingredient.getRecipeIngredients().contains(this)) {
            ingredient.getRecipeIngredients().add(this);
        }
    }


}
