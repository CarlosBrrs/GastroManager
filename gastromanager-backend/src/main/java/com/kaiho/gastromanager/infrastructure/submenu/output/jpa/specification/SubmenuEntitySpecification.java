package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SubmenuEntitySpecification {

    private static Specification<SubmenuEntity> nameContains(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    private static Specification<SubmenuEntity> descriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    private static Specification<SubmenuEntity> belongsToMenu(UUID menuUuid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("menu").get("uuid"), menuUuid);
    }

    public static Specification<SubmenuEntity> buildSpecification(SubmenuSearchCriteria criteria) {
        Specification<SubmenuEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(nameContains(criteria.search())
                    .or(descriptionContains(criteria.search())));
        }
        if (criteria.menuUuid() != null) {
            spec = spec.and(belongsToMenu(criteria.menuUuid()));
        }
        return spec;
    }
}
