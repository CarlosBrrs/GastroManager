package com.kaiho.gastromanager.domain.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RestaurantConfig {

    private boolean isPaymentRequiredBeforePlacement;
}
