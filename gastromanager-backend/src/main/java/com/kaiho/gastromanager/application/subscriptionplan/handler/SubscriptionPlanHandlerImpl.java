package com.kaiho.gastromanager.application.subscriptionplan.handler;

import com.kaiho.gastromanager.application.subscriptionplan.dto.response.SubscriptionPlanResponseDto;
import com.kaiho.gastromanager.application.subscriptionplan.mapper.SubscriptionPlanMapper;
import com.kaiho.gastromanager.domain.subscriptionplan.api.SubscriptionPlanServicePort;
import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class SubscriptionPlanHandlerImpl implements SubscriptionPlanHandler {

    private final SubscriptionPlanServicePort subscriptionPlanServicePort;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    public ApiGenericResponse<List<SubscriptionPlanResponseDto>> getAllSubscriptionPlans() {
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanServicePort.getAllSubscriptionPlans();
        var restaurantResponseDtoList = subscriptionPlanList.stream().map(subscriptionPlanMapper::toResponse).toList();
        return buildSuccessResponse("List of subscription plans retrieved successfully", restaurantResponseDtoList);

    }

    @Override
    public ApiGenericResponse<SubscriptionPlanResponseDto> getSubscriptionPlanByUUID(UUID uuid) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanServicePort.getSubscriptionPlanById(uuid);
        SubscriptionPlanResponseDto response = subscriptionPlanMapper.toResponse(subscriptionPlan);
        return buildSuccessResponse("subscription plan retrieved successfully", response);
    }
}
