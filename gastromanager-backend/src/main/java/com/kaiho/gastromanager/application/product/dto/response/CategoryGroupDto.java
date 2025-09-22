package com.kaiho.gastromanager.application.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryGroupDto(
        String name,
        List<ProductCategoryItemDto> products
) {
}
