package com.kaiho.gastromanager.domain.subscriptionplan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {
    private final UUID uuid;
    private String name;
    private String description;
    private double price;
}
