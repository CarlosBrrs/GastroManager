package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity;

import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
@Setter
public class ProductItemEntity extends Auditable implements Serializable {

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private double price;
    private boolean isEnabled;
    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItemIngredientEntity> ingredients = new ArrayList<>();

    public void addIngredient(ProductItemIngredientEntity productItemIngredientEntity) {
        ingredients.add(productItemIngredientEntity);
        productItemIngredientEntity.setProductItem(this);
    }

}
