package com.kaiho.gastromanager.application.subscriptionplan.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubscriptionPlanResponseDto(UUID uuid, String name, String description, double price) {
}
