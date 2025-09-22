package com.kaiho.gastromanager.application.order.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record UninvoicedItemResponseDto(UUID orderItemUuid, String productName, BigDecimal sellingPrice, int quantity) {
}
