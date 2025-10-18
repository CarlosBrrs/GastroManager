package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria;

import lombok.Builder;

@Builder
public record RecipeSearchCriteria(
        String search,
        Boolean isEnabled,
        String sortBy,
        String sortDirection,
        int page,
        int size
) {
}
