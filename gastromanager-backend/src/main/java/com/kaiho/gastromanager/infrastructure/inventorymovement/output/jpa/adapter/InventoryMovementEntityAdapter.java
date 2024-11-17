package com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;
import com.kaiho.gastromanager.domain.inventorymovement.spi.InventoryMovementPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.entity.InventoryMovementEntity;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.mapper.InventoryMovementEntityMapper;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.repository.InventoryMovementEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InventoryMovementEntityAdapter implements InventoryMovementPersistencePort {

    private final InventoryMovementEntityMapper inventoryMovementEntityMapper;
    private final IngredientEntityRepository ingredientEntityRepository;
    private final InventoryMovementEntityRepository inventoryMovementEntityRepository;

    @Override
    public UUID createInventoryMovement(InventoryMovement inventoryMovement) {
        InventoryMovementEntity entity = inventoryMovementEntityMapper.toEntity(inventoryMovement);
        IngredientEntity ingredientEntity = ingredientEntityRepository.findById(inventoryMovement.ingredientUuid()).orElseThrow(() -> new IngredientDoesNotExistException(inventoryMovement.ingredientUuid().toString()));
        ingredientEntity.addInventoryMovement(entity);
        return inventoryMovementEntityRepository.save(entity).getUuid();
    }
}
