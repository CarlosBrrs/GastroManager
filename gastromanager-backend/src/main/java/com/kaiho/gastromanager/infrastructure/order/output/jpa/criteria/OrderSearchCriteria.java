package com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria;

import lombok.Builder;

@Builder
public record OrderSearchCriteria(
        String search,
        String sortBy,
        String sortDirection,
        int page,
        int size
) {
}