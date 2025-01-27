package com.kaiho.gastromanager.application.inventorymovement.handler;

import com.kaiho.gastromanager.application.inventorymovement.dto.response.InventoryMovementResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class InventoryMovementHandlerImpl implements InventoryMovementHandler {

    @Override
    public ApiGenericResponse<List<InventoryMovementResponseDto>> getAllInventoryMovements() {
        return null;
    }

    @Override
    public ApiGenericResponse<InventoryMovementResponseDto> getMovementByUuid(UUID inventoryMovementUuid) {
        return null;
    }
}
