package com.kaiho.gastromanager.application.inventorymovement.mapper;

import com.kaiho.gastromanager.application.inventorymovement.dto.request.InventoryMovementRequestDto;
import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementMapper {
    public InventoryMovement toDomain(InventoryMovementRequestDto inventoryMovement) {
        if (inventoryMovement == null) {
            return null;
        }
        return InventoryMovement.builder()
                                .ingredientUuid(inventoryMovement.ingredientUuid())
                                .changeQuantity(inventoryMovement.changeQuantity())
                                .reason(inventoryMovement.reason())
                                .build();
    }
}
