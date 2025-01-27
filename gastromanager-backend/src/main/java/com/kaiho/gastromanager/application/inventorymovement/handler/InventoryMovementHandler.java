package com.kaiho.gastromanager.application.inventorymovement.handler;

import com.kaiho.gastromanager.application.inventorymovement.dto.response.InventoryMovementResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryMovementHandler {

    ApiGenericResponse<List<InventoryMovementResponseDto>> getAllInventoryMovements();

    ApiGenericResponse<InventoryMovementResponseDto> getMovementByUuid(UUID inventoryMovementUuid);
}
