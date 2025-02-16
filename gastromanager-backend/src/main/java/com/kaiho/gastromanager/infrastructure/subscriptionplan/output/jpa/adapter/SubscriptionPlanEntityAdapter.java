package com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.adapter;

import com.kaiho.gastromanager.domain.subscriptionplan.model.SubscriptionPlan;
import com.kaiho.gastromanager.domain.subscriptionplan.spi.SubscriptionPlanPersistencePort;
import com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.mapper.SubscriptionPlanEntityMapper;
import com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.repository.SubscriptionPlanEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanEntityAdapter implements SubscriptionPlanPersistencePort {
    private final SubscriptionPlanEntityRepository subscriptionPlanEntityRepository;
    private final SubscriptionPlanEntityMapper subscriptionPlanEntityMapper;

    @Override
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        return subscriptionPlanEntityRepository.findAll().stream().map(subscriptionPlanEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<SubscriptionPlan> getSubscriptionPlanById(UUID uuid) {
        return subscriptionPlanEntityRepository.findById(uuid).map(subscriptionPlanEntityMapper::toDomain);
    }
}
