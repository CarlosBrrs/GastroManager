package com.kaiho.gastromanager.infrastructure.recipe.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.entity.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;

public class RecipeEntitySpecification {

    private static Specification<RecipeEntity> nameContains(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + providedName.toLowerCase() + "%");
    }

    private static Specification<RecipeEntity> descriptionContains(String providedDescription) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                "%" + providedDescription.toLowerCase() + "%");
    }

    private static Specification<RecipeEntity> enabledOption(Boolean isEnabled) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isEnabled"),
                isEnabled);
    }

    public static Specification<RecipeEntity> buildSpecification(RecipeSearchCriteria criteria) {
        Specification<RecipeEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(nameContains(criteria.search()));
            spec = spec.and(descriptionContains(criteria.search()));
        }
        if (criteria.isEnabled() != null) {
            spec = spec.and(enabledOption(criteria.isEnabled()));
        }
        return spec;
    }
}
