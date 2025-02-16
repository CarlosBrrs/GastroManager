package com.kaiho.gastromanager.domain.subscriptionplan.api;

import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;
import com.kaiho.gastromanager.domain.subscriptionplan.spi.SubscriptionPlanPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanUseCase implements SubscriptionPlanServicePort {
    private final SubscriptionPlanPersistencePort subscriptionPlanPersistencePort;

    @Override
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        return subscriptionPlanPersistencePort.getAllSubscriptionPlans();
    }

    @Override
    public SubscriptionPlan getSubscriptionPlanById(UUID uuid) {
        return subscriptionPlanPersistencePort.getSubscriptionPlanById(uuid).orElseThrow();
    }
}
