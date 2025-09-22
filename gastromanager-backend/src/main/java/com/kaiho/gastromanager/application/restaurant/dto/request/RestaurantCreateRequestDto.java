package com.kaiho.gastromanager.application.restaurant.dto.request;

import com.kaiho.gastromanager.application.auth.dto.request.RestaurantCreateAddressRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.RestaurantCreateContactRequestDto;

public record RestaurantCreateRequestDto(String name,
                                         String description,
                                         RestaurantCreateAddressRequestDto address,
                                         RestaurantCreateContactRequestDto contact,
                                         RestaurantConfigCreateRequestDto configs) {
}
