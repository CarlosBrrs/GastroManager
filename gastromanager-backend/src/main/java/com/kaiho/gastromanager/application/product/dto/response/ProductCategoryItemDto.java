package com.kaiho.gastromanager.application.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductCategoryItemDto(
        UUID id,
        String name,
        BigDecimal price,
        String category,
        String description,
        boolean isEnabled
) {
}
