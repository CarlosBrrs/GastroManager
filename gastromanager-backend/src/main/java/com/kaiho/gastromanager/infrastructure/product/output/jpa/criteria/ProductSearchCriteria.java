package com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria;

import lombok.Builder;

@Builder
public record ProductSearchCriteria(
        String search,
        String sortBy,
        String sortDirection,
        int page,
        int size
) {
}