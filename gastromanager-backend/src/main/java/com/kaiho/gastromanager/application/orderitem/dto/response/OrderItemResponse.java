package com.kaiho.gastromanager.application.orderitem.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemResponse(UUID productItemUuid, int quantity, double unitPrice) {
}
