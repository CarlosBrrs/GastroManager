package com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.subscriptionplan.output.jpa.entity.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionPlanEntityRepository extends JpaRepository<SubscriptionPlanEntity, UUID> {
}
