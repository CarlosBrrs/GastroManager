package com.kaiho.gastromanager.application.orderitem.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponseDto(UUID uuid, UUID productUuid, String productName, int quantity,
                                   BigDecimal unitPrice, BigDecimal subtotal, String customerNotes) {
}
