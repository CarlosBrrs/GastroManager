package com.kaiho.gastromanager.application.subscriptionplan.handler;

import com.kaiho.gastromanager.application.subscriptionplan.dto.response.SubscriptionPlanResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanHandler {
    ApiGenericResponse<List<SubscriptionPlanResponseDto>> getAllSubscriptionPlans();

    ApiGenericResponse<SubscriptionPlanResponseDto> getSubscriptionPlanByUUID(UUID uuid);
}
