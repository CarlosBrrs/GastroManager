package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria;

import lombok.Builder;

@Builder
public record IngredientSearchCriteria(
        String search,
        String category,
        String sortBy,// campo para ordenar
        String sortDirection, // "asc" o "desc"
        int page,
        int size
) {
}