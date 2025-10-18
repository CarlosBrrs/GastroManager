package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.adapter;

import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.recipe.spi.RecipePersistencePort;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.mapper.RecipeEntityMapper;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.repository.RecipeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static com.kaiho.gastromanager.infrastructure.recipe.output.jpa.specification.RecipeEntitySpecification.buildSpecification;

@Component
@RequiredArgsConstructor
public class RecipeEntityAdapter implements RecipePersistencePort {

    private final RecipeEntityMapper recipeEntityMapper;
    private final RecipeEntityRepository recipeEntityRepository;

    @Override
    public Recipe createRecipe(Recipe recipe) {

        RecipeEntity recipeEntity = recipeEntityMapper.toEntity(recipe);
        RecipeEntity savedRecipeEntity = recipeEntityRepository.save(recipeEntity);

        return recipeEntityMapper.toDomain(savedRecipeEntity);
    }

    @Override
    public Page<Recipe> getAllRecipes(RecipeSearchCriteria criteria, Pageable pageable) {
        Specification<RecipeEntity> spec = buildSpecification(criteria);
        Page<RecipeEntity> entityList = recipeEntityRepository.findAll(spec, pageable);
        return entityList.map(recipeEntityMapper::toDomain);
    }

    @Override
    public Optional<Recipe> getRecipeByUuid(UUID recipeUuid) {
        UUID currentRestaurant = getCurrentRestaurant();

        return recipeEntityRepository.findById(recipeUuid, currentRestaurant)
                                     .map(recipeEntityMapper::toDomain);
    }

    @Override
    public boolean recipeExistsByName(String name) {
        UUID restaurantUuid = getCurrentRestaurant();
        return recipeEntityRepository.existsByNameAndRestaurantUuid(name, restaurantUuid);
    }

    @Override
    public boolean recipeExistsByUuid(UUID uuid, UUID currentRestaurant) {
        return recipeEntityRepository.existsByUuid(uuid, currentRestaurant);
    }
}
