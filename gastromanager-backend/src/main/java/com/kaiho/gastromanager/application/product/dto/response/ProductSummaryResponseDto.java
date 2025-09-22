package com.kaiho.gastromanager.application.product.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductSummaryResponseDto(UUID uuid, String name, String description, double purchasePrice, double salePrice) {
}
