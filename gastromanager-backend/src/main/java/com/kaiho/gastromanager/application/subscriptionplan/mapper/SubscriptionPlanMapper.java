package com.kaiho.gastromanager.application.subscriptionplan.mapper;

import com.kaiho.gastromanager.application.subscriptionplan.dto.response.SubscriptionPlanResponseDto;
import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {
    public SubscriptionPlanResponseDto toResponse(SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) {
            return null;
        }
        return SubscriptionPlanResponseDto.builder()
                .uuid(subscriptionPlan.getUuid())
                .name(subscriptionPlan.getName())
                .description(subscriptionPlan.getDescription())
                .price(subscriptionPlan.getPrice())
                .build();
    }
}
