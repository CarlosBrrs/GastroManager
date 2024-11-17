package com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.mapper;

import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;
import com.kaiho.gastromanager.infrastructure.inventorymovement.output.jpa.entity.InventoryMovementEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementEntityMapper {
    public InventoryMovementEntity toEntity(InventoryMovement inventoryMovement) {
        if (inventoryMovement == null) {
            return null;
        }
        return InventoryMovementEntity.builder()
                .changeQuantity(inventoryMovement.changeQuantity())
                .reason(inventoryMovement.reason())
                .build();
    }
}
