package com.kaiho.gastromanager.application.restaurant.dto.request;

import lombok.Builder;

@Builder
public record RestaurantRequestDto(String name, String address, String description) {
}
