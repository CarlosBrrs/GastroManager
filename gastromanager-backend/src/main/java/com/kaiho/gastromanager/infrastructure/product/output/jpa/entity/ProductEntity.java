package com.kaiho.gastromanager.infrastructure.product.output.jpa.entity;

import com.kaiho.gastromanager.domain.product.model.ProductMode;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "products")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductEntity extends Auditable implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductMode mode;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid", nullable = false)
    private RestaurantEntity restaurant;

    // Relaciones para modo avanzado
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductRecipeEntity> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductIngredientEntity> ingredients = new ArrayList<>();

    // Métodos helper para manejar las relaciones bidireccionales
    public void addRecipe(ProductRecipeEntity recipe) {
        recipes.add(recipe);
        recipe.setProduct(this);
    }

    public void addIngredient(ProductIngredientEntity ingredient) {
        ingredients.add(ingredient);
        ingredient.setProduct(this);
    }
}
