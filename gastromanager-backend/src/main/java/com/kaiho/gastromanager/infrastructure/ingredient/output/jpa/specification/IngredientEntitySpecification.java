package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import org.springframework.data.jpa.domain.Specification;

public class IngredientEntitySpecification {

    private static Specification<IngredientEntity> nameContains(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    public static Specification<IngredientEntity> buildSpecification(IngredientSearchCriteria criteria) {
        Specification<IngredientEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(nameContains(criteria.search()));
        }
        return spec;
    }
}
