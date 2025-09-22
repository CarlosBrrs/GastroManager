package com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.mapper;

import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.entity.RecipeIngredientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeIngredientEntityMapper {

    private final IngredientEntityMapper ingredientEntityMapper;

    public RecipeIngredientEntity toEntity(RecipeIngredient recipeIngredient) {
        if (recipeIngredient == null) {
            return null;
        }
        RecipeIngredientEntity build = RecipeIngredientEntity.builder()

                                                             .quantity(recipeIngredient.getQuantity())
                                                             .build();

        build.setIngredient(ingredientEntityMapper.toEntity(recipeIngredient.getIngredient()));

        return build;
    }

    public RecipeIngredient toDomain(RecipeIngredientEntity recipeIngredientEntity) {
        if (recipeIngredientEntity == null) {
            return null;
        }
        return RecipeIngredient.builder()
                               .ingredient(ingredientEntityMapper.toDomain(recipeIngredientEntity.getIngredient()))
                               .quantity(recipeIngredientEntity.getQuantity())
                               .build();
    }

    public List<RecipeIngredient> toDomainList(List<RecipeIngredientEntity> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return null;
        }
        return ingredients.stream()
                          .map(this::toDomain)
                          .toList();
    }
}
