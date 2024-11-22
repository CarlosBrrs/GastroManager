package com.kaiho.gastromanager.domain.inventorymovement.usecase;

import com.kaiho.gastromanager.domain.inventorymovement.api.InventoryMovementServicePort;
import com.kaiho.gastromanager.domain.inventorymovement.exception.InvalidInventoryMovementQuantityException;
import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;
import com.kaiho.gastromanager.domain.inventorymovement.spi.InventoryMovementPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryMovementUseCasePort implements InventoryMovementServicePort {

    private final InventoryMovementPersistencePort inventoryMovementPersistencePort;

//    @Override
//    public UUID createInventoryMovement(InventoryMovement movement) {
//        if (movement.changeQuantity() == 0) {
//            throw new InvalidInventoryMovementQuantityException();
//        }
//        return inventoryMovementPersistencePort.createInventoryMovement(movement);
//    }

    @Override
    public void recordInventoryMovement(UUID ingredientUuid, int changeQuantity, String reason) {
        InventoryMovement movement = InventoryMovement.builder()
                .ingredientUuid(ingredientUuid)
                .changeQuantity(changeQuantity)
                .reason(reason)
                .build();
        if (movement.changeQuantity() == 0) {
            throw new InvalidInventoryMovementQuantityException();
        }
        UUID inventoryMovement = inventoryMovementPersistencePort.createInventoryMovement(movement);
        log.info("Movement created with uuid" + inventoryMovement);
    }

    @Override
    public void recordInventoryMovements(Map<UUID, Integer> changeQuantities, String reason) {

    }
}
