package com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria;

import lombok.Builder;

@Builder
public record MenuSearchCriteria(
        String search,
        String sortBy,
        String sortDirection,
        int page,
        int size
) {
}