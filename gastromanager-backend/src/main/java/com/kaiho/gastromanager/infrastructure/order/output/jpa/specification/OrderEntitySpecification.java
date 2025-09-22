package com.kaiho.gastromanager.infrastructure.order.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderEntitySpecification {

    private static Specification<OrderEntity> codeContains(String providedCode) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + providedCode.toLowerCase() + "%");
    }

    public static Specification<OrderEntity> buildSpecification(OrderSearchCriteria criteria) {
        Specification<OrderEntity> spec = Specification.where(null);
        if (criteria.search() != null) {
            spec = spec.and(codeContains(criteria.search()));
        }
        return spec;
    }
}
