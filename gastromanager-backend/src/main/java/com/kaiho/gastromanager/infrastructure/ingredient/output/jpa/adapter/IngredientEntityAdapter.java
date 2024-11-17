package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class IngredientEntityAdapter implements IngredientPersistencePort {

    private final IngredientEntityRepository ingredientEntityRepository;
    private final IngredientEntityMapper ingredientEntityMapper;


    @Override
    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> entityList = ingredientEntityRepository.findAll();
        return entityList.stream().map(ingredientEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Ingredient> getIngredientByUuid(UUID uuid) {
        return ingredientEntityRepository.findById(uuid).map(ingredientEntityMapper::toDomain);
    }

    @Override
    public boolean ingredientExistsByName(String name) {
        return ingredientEntityRepository.existsByName(name);
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

        existingEntity.setName(ingredient.name());
        existingEntity.setUnit(ingredient.unit());
        existingEntity.setSupplier(ingredient.supplier());
        existingEntity.setMinimumStockQuantity(ingredient.minimumStockQuantity());
        existingEntity.setPricePerUnit(ingredient.pricePerUnit());

//        IngredientEntity entity = ingredientEntityMapper.toEntity(existingEntity);
        IngredientEntity saved = ingredientEntityRepository.save(existingEntity);
        return ingredientEntityMapper.toDomain(saved);
    }

    @Override
    public UUID updateIngredientStock(UUID ingredientUuid, int newStock) {
        IngredientEntity existingEntity = ingredientEntityRepository.findById(ingredientUuid).orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));
        existingEntity.setAvailableStock(newStock);
        return existingEntity.getUuid();
    }

}
