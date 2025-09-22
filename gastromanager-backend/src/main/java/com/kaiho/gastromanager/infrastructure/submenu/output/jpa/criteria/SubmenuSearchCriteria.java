package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmenuSearchCriteria(
        String search,
        String sortBy,
        String sortDirection,
        int page,
        int size,
        UUID menuUuid
) {
}