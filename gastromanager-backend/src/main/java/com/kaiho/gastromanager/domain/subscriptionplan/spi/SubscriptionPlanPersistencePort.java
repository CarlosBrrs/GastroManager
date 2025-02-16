package com.kaiho.gastromanager.domain.subscriptionplan.spi;

import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanPersistencePort {
    List<SubscriptionPlan> getAllSubscriptionPlans();

    Optional<SubscriptionPlan> getSubscriptionPlanById(UUID uuid);
}
