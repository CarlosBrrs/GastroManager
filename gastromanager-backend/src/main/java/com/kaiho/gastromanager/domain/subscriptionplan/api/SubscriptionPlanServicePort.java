package com.kaiho.gastromanager.domain.subscriptionplan.api;

import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;

import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanServicePort {
    List<SubscriptionPlan> getAllSubscriptionPlans();

    SubscriptionPlan getSubscriptionPlanById(UUID uuid);
}
