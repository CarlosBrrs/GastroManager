package com.kaiho.gastromanager.domain.recipe.model;

import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Recipe {
    private UUID uuid;
    private String name;
    private String description;
    private BigDecimal cost;
    //    private List<String> tags;
    private BaseRecipe baseRecipe;
    private boolean isEnabled;
    private Restaurant restaurant;
    private List<RecipeIngredient> ingredients;
    private String createdBy;
    private String updatedBy;
    private Instant createdDate;
    private Instant updatedDate;
}
