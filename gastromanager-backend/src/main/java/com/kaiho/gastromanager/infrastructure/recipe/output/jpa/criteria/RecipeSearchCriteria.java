package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria;

import lombok.Builder;

@Builder
public record RecipeSearchCriteria(
        String search,
        Boolean isEnabled,
        String sortBy,// campo para ordenar
        String sortDirection, // "asc" o "desc"
        int page,
        int size
) {
}
