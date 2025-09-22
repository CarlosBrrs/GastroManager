package com.kaiho.gastromanager.application.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductGroupByResponseDto(
        List<CategoryGroupDto> categories,
        int totalProducts,
        int totalCategories
) {
}
