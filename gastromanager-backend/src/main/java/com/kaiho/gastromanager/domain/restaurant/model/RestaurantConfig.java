package com.kaiho.gastromanager.domain.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RestaurantConfig {

    private UUID uuid;
    private boolean payBeforeOrder;
    private boolean isFranchise;
    private Restaurant restaurant;
}
