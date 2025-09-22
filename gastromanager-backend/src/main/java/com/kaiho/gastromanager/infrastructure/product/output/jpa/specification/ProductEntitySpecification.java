package com.kaiho.gastromanager.infrastructure.product.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductEntitySpecification {

    private static Specification<ProductEntity> nameContains(String providedName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    private static Specification<ProductEntity> descriptionContains(String providedDescription) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                "%" + providedDescription.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> buildSpecification(ProductSearchCriteria criteria) {
        Specification<ProductEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(nameContains(criteria.search()));
            spec = spec.and(descriptionContains(criteria.search()));
        }
        return spec;
    }

    public static Specification<ProductEntity> buildGroupBySpecification(ProductGroupByCriteria criteria, UUID restaurantUuid) {
        Specification<ProductEntity> spec = Specification.where(restaurantEquals(restaurantUuid));

        if (criteria.isEnabled() != null) {
            spec = spec.and(isEnabledEquals(criteria.isEnabled()));
        }

        return spec;
    }

    private static Specification<ProductEntity> restaurantEquals(UUID restaurantUuid) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("restaurant").get("uuid"), restaurantUuid);
    }

    private static Specification<ProductEntity> isEnabledEquals(Boolean isEnabled) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("isEnabled"), isEnabled);
    }
}
