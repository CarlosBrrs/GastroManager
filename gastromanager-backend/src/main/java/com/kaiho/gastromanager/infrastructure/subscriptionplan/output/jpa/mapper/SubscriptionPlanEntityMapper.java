package com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.mapper;

import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;
import com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.entity.SubscriptionPlanEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanEntityMapper {
    public SubscriptionPlan toDomain(SubscriptionPlanEntity subscriptionPlanEntity) {
        if (subscriptionPlanEntity == null) {
            return null;
        }
        return SubscriptionPlan.builder()
                .uuid(subscriptionPlanEntity.getUuid())
                .name(subscriptionPlanEntity.getName())
                .description(subscriptionPlanEntity.getDescription())
                .price(subscriptionPlanEntity.getPrice())
                .build();
    }
}
