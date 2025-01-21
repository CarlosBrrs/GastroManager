package com.kaiho.gastromanager.application.inventorymovement.handler;

import com.kaiho.gastromanager.application.inventorymovement.dto.request.InventoryMovementRequestDto;
import com.kaiho.gastromanager.application.inventorymovement.dto.response.InventoryMovementResponseDto;
import com.kaiho.gastromanager.domain.inventorymovement.api.InventoryMovementServicePort;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@RequiredArgsConstructor
@Component
public class InventoryMovementHandlerImpl implements InventoryMovementHandler {

    private final InventoryMovementServicePort inventoryMovementServicePort;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public ApiGenericResponse<UUID> createInventoryMovement(InventoryMovementRequestDto inventoryMovement) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());
        inventoryMovementServicePort.recordInventoryMovement(inventoryMovement.ingredientUuid(), inventoryMovement.changeQuantity(), inventoryMovement.reason(), restaurant);
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
