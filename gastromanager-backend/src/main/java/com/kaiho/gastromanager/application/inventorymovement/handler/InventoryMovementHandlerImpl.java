package com.kaiho.gastromanager.application.inventorymovement.handler;

import com.kaiho.gastromanager.application.inventorymovement.dto.request.InventoryMovementRequestDto;
import com.kaiho.gastromanager.application.inventorymovement.dto.response.InventoryMovementResponseDto;
import com.kaiho.gastromanager.application.inventorymovement.mapper.InventoryMovementMapper;
import com.kaiho.gastromanager.domain.inventorymovement.api.InventoryMovementServicePort;
import com.kaiho.gastromanager.domain.inventorymovement.model.InventoryMovement;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class InventoryMovementHandlerImpl implements InventoryMovementHandler {
    private final InventoryMovementMapper inventoryMovementMapper;
    private final InventoryMovementServicePort inventoryMovementServicePort;

    @Override
    public ApiGenericResponse<UUID> createInventoryMovement(InventoryMovementRequestDto inventoryMovement) {
        inventoryMovementServicePort.recordInventoryMovement(inventoryMovement.ingredientUuid(), inventoryMovement.changeQuantity(), inventoryMovement.reason());
        return buildSuccessResponse("Movement registered successfully. No data to send back to client", null);
    }

    @Override
    public ApiGenericResponse<List<InventoryMovementResponseDto>> getAllInventoryMovements() {
        return null;
    }

    @Override
    public ApiGenericResponse<InventoryMovementResponseDto> getMovementByUuid(UUID inventoryMovementUuid) {
        return null;
    }
}
