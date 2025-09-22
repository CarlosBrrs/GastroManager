package com.kaiho.gastromanager.application.restaurant.dto.request;

import com.kaiho.gastromanager.application.auth.dto.request.RestaurantCreateAddressRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.RestaurantCreateContactRequestDto;
import lombok.Builder;

@Builder
public record RestaurantRequestDto(String name, RestaurantCreateAddressRequestDto address, String description,
                                   RestaurantCreateContactRequestDto contact) {
}
