package com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria;

import lombok.Builder;

@Builder
public record ProductGroupByCriteria(
        String groupBy,
        boolean includeEmpty,
        Boolean isEnabled
) {
}
