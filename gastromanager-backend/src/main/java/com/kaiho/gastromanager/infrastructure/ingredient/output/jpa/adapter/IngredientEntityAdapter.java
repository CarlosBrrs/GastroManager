package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.specification.IngredientEntitySpecification.buildSpecification;

@RequiredArgsConstructor
@Repository
public class IngredientEntityAdapter implements IngredientPersistencePort {

    private final IngredientEntityRepository ingredientEntityRepository;
    private final IngredientEntityMapper ingredientEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;


    @Override
    public Page<Ingredient> getAllIngredients(IngredientSearchCriteria criteria, Pageable pageable) {
        Specification<IngredientEntity> spec = buildSpecification(criteria);
        Page<IngredientEntity> entityList = ingredientEntityRepository.findAll(spec, pageable);
        return entityList.map(ingredientEntityMapper::toDomain);
    }

    @Override
    public Optional<Ingredient> getIngredientByUuid(UUID uuid) {
        UUID currentRestaurant = getCurrentRestaurant();
        Optional<Ingredient> ingredient = ingredientEntityRepository.findById(uuid, currentRestaurant)
                                                                    .map(ingredientEntityMapper::toDomain);
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(currentRestaurant)
                                                                      .orElseThrow(() -> new RestaurantDoesNotExistException(currentRestaurant.toString()));
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
        IngredientEntity existingEntity = ingredientEntityRepository.findById(uuid, ingredient.getRestaurant().getUuid()).orElseThrow(() -> new IngredientDoesNotExistException(uuid.toString()));

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

    @Override
    public boolean existsByUuid(UUID uuid, UUID currentRestaurant) {
        return ingredientEntityRepository.existsByUuid(uuid, currentRestaurant);
    }

    @Override
    public List<Ingredient> getAllIngredientsByRestaurant() {
        List<IngredientEntity> entities = ingredientEntityRepository.findAll();
        return entities.stream()
                .map(ingredientEntityMapper::toDomain)
                .toList();
    }

}
