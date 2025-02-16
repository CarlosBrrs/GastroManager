package com.kaiho.gastromanager.application.orderitem.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemResponseDto(UUID productItemUuid, String productItemName, int quantity, double unitPrice) {
}
