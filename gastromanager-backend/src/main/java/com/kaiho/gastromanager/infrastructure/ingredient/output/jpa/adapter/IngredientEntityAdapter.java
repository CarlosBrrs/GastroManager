package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class IngredientEntityAdapter implements IngredientPersistencePort {

    private final IngredientEntityRepository ingredientEntityRepository;
    private final IngredientEntityMapper ingredientEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;


    @Override
    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> entityList = ingredientEntityRepository.findAll();
        return entityList.stream().map(ingredientEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Ingredient> getIngredientByUuid(UUID uuid, UUID restaurantUuid) {
        Optional<Ingredient> ingredient = ingredientEntityRepository.findById(uuid, restaurantUuid).map(ingredientEntityMapper::toDomain);
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(restaurantUuid).orElseThrow(() -> new RestaurantDoesNotExistException(restaurantUuid.toString()));
        Restaurant restaurant = restaurantEntityMapper.toDomain(restaurantEntity);
        ingredient.ifPresent(ing -> ing.setRestaurant(restaurant));
        return ingredient;
    }

    @Override
    public boolean ingredientExistsByName(String name, UUID restaurantUuid) {
        return ingredientEntityRepository.existsByName(name, restaurantUuid);
    }

    @Override
    public UUID addIngredient(Ingredient ingredient) {
        IngredientEntity entity = ingredientEntityMapper.toEntity(ingredient);
        IngredientEntity saved = ingredientEntityRepository.save(entity);
        return saved.getUuid();
    }

    @Override
    public Ingredient updateIngredient(UUID uuid, Ingredient ingredient) {
        IngredientEntity existingEntity = ingredientEntityRepository.findById(uuid).orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));

        existingEntity.setName(ingredient.getName());
        existingEntity.setUnit(ingredient.getUnit());
        existingEntity.setSupplier(ingredient.getSupplier());
        existingEntity.setMinimumStockQuantity(ingredient.getMinimumStockQuantity());
        existingEntity.setPricePerUnit(ingredient.getPricePerUnit());

        IngredientEntity saved = ingredientEntityRepository.save(existingEntity);
        return ingredientEntityMapper.toDomain(saved);
    }

    @Override
    public UUID updateIngredientStock(UUID ingredientUuid, int newStock) {
        IngredientEntity existingEntity = ingredientEntityRepository.findById(ingredientUuid).orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));
        existingEntity.setAvailableStock(newStock);
        return existingEntity.getUuid();
    }

    @Override
    public List<Ingredient> findIngredientsByUuids(Set<UUID> uuids) {
        return ingredientEntityRepository.findAllById(uuids).stream().map(ingredientEntityMapper::toDomain).toList();
    }

    @Override
    public void updateIngredientsStock(Map<UUID, Double> newAvailableStocks) {
        newAvailableStocks.forEach(ingredientEntityRepository::updateStockByUuid);
    }

    @Override
    public Optional<Restaurant> getRestaurantByIngredientUuid(UUID ingredientUuid) {
        IngredientEntity ingredientEntity = ingredientEntityRepository.findById(ingredientUuid).orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));
        return Optional.ofNullable(restaurantEntityMapper.toDomain(ingredientEntity.getRestaurant()));

    }

}
