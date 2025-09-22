package com.kaiho.gastromanager.application.restaurant.dto.request;

public record RestaurantConfigCreateRequestDto(boolean isFranchise,
                                               boolean payBeforeOrder,
                                               boolean enableDelivery) {
}
