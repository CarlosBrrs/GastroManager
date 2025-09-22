package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.mapper;

import com.kaiho.gastromanager.domain.recipe.model.BaseRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import com.kaiho.gastromanager.infrastructure.recipeingredient.output.jpa.mapper.RecipeIngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class RecipeEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantServicePort restaurantServicePort;
    private final RecipeIngredientEntityMapper recipeIngredientEntityMapper;


    public RecipeEntity toEntity(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(recipe.getRestaurant().getUuid())
                                                                      .orElseThrow(() -> new RestaurantDoesNotExistException(recipe.getRestaurant().getUuid().toString()));


        RecipeEntity entity = RecipeEntity.builder()
                                          .uuid(recipe.getUuid())
                                          .name(recipe.getName())
                                          .description(recipe.getDescription())
                                          .restaurant(restaurantEntity)
                                          .cost(recipe.getCost())
                                          .isEnabled(recipe.isEnabled())
                                          .ingredients(new ArrayList<>())
                                          .build();

        recipe.getIngredients().stream()
              .map(recipeIngredientEntityMapper::toEntity)
              .forEach(entity::addIngredient);

        if (recipe.getBaseRecipe() != null) {
            RecipeEntity baseEntity = new RecipeEntity();
            baseEntity.setUuid(recipe.getBaseRecipe().getRecipe().getUuid());
            baseEntity.setBaseRecipePortion(recipe.getBaseRecipe().getPortion());
            entity.setBaseRecipe(baseEntity);
        }
        return entity;
    }

    public Recipe toDomain(RecipeEntity entity) {
        if (entity == null) {
            return null;
        }
        BaseRecipe baseRecipe = null;
        if (entity.getBaseRecipe() != null) {
            baseRecipe = BaseRecipe.builder()
                                   .recipe(toDomain(entity.getBaseRecipe()))
                                   .portion(entity.getBaseRecipePortion())
                                   .build();
        }
        return Recipe.builder()
                     .uuid(entity.getUuid())
                     .name(entity.getName())
                     .description(entity.getDescription())
                     .cost(entity.getCost())
                     .isEnabled(entity.isEnabled())
                     .restaurant(restaurantServicePort.getRestaurantById(getCurrentRestaurant()))
                     .ingredients(recipeIngredientEntityMapper.toDomainList(entity.getIngredients()))
                     .baseRecipe(baseRecipe)
                     .createdBy(entity.getCreatedBy())
                     .createdDate(entity.getCreatedDate())
                     .updatedBy(entity.getUpdatedBy())
                     .updatedDate(entity.getUpdatedDate())
                     .build();
    }
}
