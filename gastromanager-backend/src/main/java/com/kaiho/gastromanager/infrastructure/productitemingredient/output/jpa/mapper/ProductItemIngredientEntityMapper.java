package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class ProductItemIngredientEntityMapper {

    private final IngredientEntityMapper ingredientEntityMapper;
    private final IngredientEntityRepository ingredientEntityRepository;

    public ProductItemIngredient toDomain(ProductItemIngredientEntity productItemIngredientEntity) {
        if (productItemIngredientEntity == null) {
            return null;
        }

        Ingredient ingredient = ingredientEntityMapper.toDomain(productItemIngredientEntity.getIngredient());

        return ProductItemIngredient.builder()
                .uuid(productItemIngredientEntity.getUuid())
                .ingredient(ingredient)
                .quantity(productItemIngredientEntity.getQuantity())
                .build();
    }

    public ProductItemIngredientEntity toEntity(ProductItemIngredient productItemIngredient) {
        if (productItemIngredient == null) {
            return null;
        }

        IngredientEntity ingredientEntity = ingredientEntityRepository
                .findById(productItemIngredient.getIngredient().getUuid(), getCurrentRestaurant())
                .orElseThrow(() -> new IngredientDoesNotExistException(productItemIngredient.getIngredient().getUuid().toString()));

        return ProductItemIngredientEntity.builder()
                .ingredient(ingredientEntity)
                .quantity(productItemIngredient.getQuantity())
                .build();
    }
}
