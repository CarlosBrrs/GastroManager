package com.kaiho.gastromanager.infrastructure.menu.output.specification;

import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import org.springframework.data.jpa.domain.Specification;

public class MenuEntitySpecification {

    private static Specification<MenuEntity> nameContains(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    public static Specification<MenuEntity> buildSpecification(MenuSearchCriteria criteria) {
        Specification<MenuEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(nameContains(criteria.search()));
        }
        return spec;
    }
}
