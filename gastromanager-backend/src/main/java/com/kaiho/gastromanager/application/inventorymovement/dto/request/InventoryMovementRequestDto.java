package com.kaiho.gastromanager.application.inventorymovement.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryMovementRequestDto(UUID ingredientUuid,
                                          int changeQuantity,
                                          String reason

) {
}
