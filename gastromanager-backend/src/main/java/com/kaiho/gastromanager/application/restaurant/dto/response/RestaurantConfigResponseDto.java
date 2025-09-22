package com.kaiho.gastromanager.application.restaurant.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RestaurantConfigResponseDto(UUID uuid, boolean requirePaymentBeforeOrder) {
}
